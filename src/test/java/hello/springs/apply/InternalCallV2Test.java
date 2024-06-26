package hello.springs.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV2Test {

    @Autowired
    CallService callService;
    @Autowired
    InternalCallService internalCallService;

    @Test
    void printProxy() {
        log.info("callService class : {}", callService.getClass());
    }

    @Test
    void internallCall() {
        internalCallService.internal();
    }

    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV1TestConfig {
        @Bean
        CallService callService() {
            return new CallService(internalCallService());
        }

        @Bean
        InternalCallService internalCallService() {
            return new InternalCallService();
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService {

        private final InternalCallService internalCallService;

        public void external() {
            log.info("call external");
            printTxInfo();
            internalCallService.internal();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive = {}", txActive);
        }
    }

    static class InternalCallService {

        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }
        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive = {}", txActive);
        }
    }
}
