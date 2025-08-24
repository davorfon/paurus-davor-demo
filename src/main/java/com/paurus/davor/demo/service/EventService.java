package com.paurus.davor.demo.service;

import com.paurus.davor.demo.helper.MatchEventParser;
import com.paurus.davor.demo.model.MatchEvent;
import com.paurus.davor.demo.model.MatchEventMinMaxDate;
import com.paurus.davor.demo.repository.MatchEventRepository;
import com.paurus.davor.demo.util.ParallelEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.paurus.davor.demo.util.Const.DT_HHmmss;
import static com.paurus.davor.demo.util.Const.yyyyMMdd_HHmmss;

@Service
public class EventService {

    private static final Logger log = LogManager.getLogger(EventService.class);

    private static final int BATCH_SIZE = 2500;
    private static final int WAIT_TIMEOUT_MS = 30 * 1000;
    private static final int THREADS = 4;

    private final DataSource batchDataSource;

    @Autowired
    private MatchEventRepository matchEventRepo;

    public EventService(@Qualifier("batchDataSource") DataSource batchDataSource) {
        this.batchDataSource = batchDataSource;
    }

    public void processEvents() {

        long startMillis = System.currentTimeMillis();
        log.info("processEvents started at {}", LocalTime.now().format(DT_HHmmss));

        ParallelEventExecutor executor = new ParallelEventExecutor(THREADS, 5, TimeUnit.MINUTES);

        Map<String, PriorityQueue<MatchEvent>> eventMap = new HashMap<>();
        Map<String, Long> lastInsertTime = new ConcurrentHashMap<>();


        try (InputStream is = getClass().getClassLoader().getResourceAsStream("fo_random.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            reader.lines().forEach(line -> processEvent(line, executor, eventMap, lastInsertTime));

            for (String matchId : eventMap.keySet()) {

                PriorityQueue<MatchEvent> queue = eventMap.get(matchId);
                if (queue != null && !queue.isEmpty()) {
                    insertBatch(queue, executor, lastInsertTime);
                }
            }

        } catch (Exception e) {
            log.error("processEvents", e);
        } finally {
            executor.shutdownAndAwait();

            long elapsedMillis = System.currentTimeMillis() - startMillis;
            log.info("processEvents finished at {}. Time elapsed: {} ms", LocalTime.now().format(DT_HHmmss), elapsedMillis);

            MatchEventMinMaxDate minMaxDate = matchEventRepo.findMatchEventMinMaxDateInsert();
            log.info("Min insert date {}", yyyyMMdd_HHmmss.format(minMaxDate.getMinDateInsert()));
            log.info("Max insert date {}", yyyyMMdd_HHmmss.format(minMaxDate.getMaxDateInsert()));
        }
    }


    private void processEvent(String line, ParallelEventExecutor executor, Map<String, PriorityQueue<MatchEvent>> eventMap, Map<String, Long> lastInsertTime) {

        try {
            if (isHeaderLine(line)) {
                return;
            }

            MatchEvent matchEvent = MatchEventParser.parse(line);

            PriorityQueue<MatchEvent> queue = eventMap.computeIfAbsent(matchEvent.getMatchId(), k -> new PriorityQueue<>());
            queue.offer(matchEvent);

            if (batchSizeReached(queue) || insertTimeoutExceeded(matchEvent, lastInsertTime)) {
                insertBatch(queue, executor, lastInsertTime);
            }
        } catch (Exception e) {
            log.error("processEvent", e);
        }
    }

    private boolean isHeaderLine(String line) {
        return line.contains("MATCH_ID|MARKET_ID|OUTCOME_ID|SPECIFIERS");
    }


    private void insertBatch(PriorityQueue<MatchEvent> queue, ParallelEventExecutor executor, Map<String, Long> lastInsertTime) {

        if (queue.isEmpty()) {
            return;
        }

        PriorityQueue<MatchEvent> copy = new PriorityQueue<>(queue);
        queue.clear();

        String matchId = copy.peek().getMatchId();

        executor.submit(() -> {

            boolean insertSuccessful = insertBatchToDb(copy);
            if (insertSuccessful) {
                lastInsertTime.put(matchId, System.currentTimeMillis());
            }
        }, matchId);
    }

    private boolean batchSizeReached(PriorityQueue<MatchEvent> queue) {
        return queue.size() >= BATCH_SIZE;
    }

    private boolean insertTimeoutExceeded(MatchEvent matchEvent, Map<String, Long> lastInsertTime) {

        long now = System.currentTimeMillis();
        long lastInsert = lastInsertTime.getOrDefault(matchEvent.getMatchId(), now);

        return (now - lastInsert >= WAIT_TIMEOUT_MS);
    }

    private boolean insertBatchToDb(PriorityQueue<MatchEvent> queue) {

        String sql = "INSERT INTO match_events (match_id, market_id, outcome_id, specifiers) VALUES (?, ?, ?, ?);";

        try (Connection conn = batchDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            while (!queue.isEmpty()) {

                MatchEvent matchEvent = queue.poll();

                ps.setString(1, matchEvent.getMatchId());
                ps.setLong(2, matchEvent.getMarketId());
                ps.setString(3, matchEvent.getOutcomeId());
                ps.setString(4, matchEvent.getSpecifiers());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();

            return true;
        } catch (Exception e) {
            log.error("insertBatchToDb", e);
            return false;
        }
    }

}
