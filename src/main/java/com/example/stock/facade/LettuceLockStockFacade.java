package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {

    private RedisLockRepository repository;
    private StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository repository, StockService stockService) {
        this.repository = repository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!repository.lock(id)) { // 락을 획득하지 못했기 때문에 계속 요청
            Thread.sleep(100);
            System.out.println("재시도! lettucelock ");
        }

        try {
            stockService.decrease(id, quantity);
        }finally {
            repository.unlock(id);
        }
    }
}
