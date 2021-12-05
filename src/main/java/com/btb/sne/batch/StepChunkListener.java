package com.btb.sne.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

@Slf4j
public class StepChunkListener implements ChunkListener {

    @Override
    public void afterChunk(ChunkContext context) {
        log.info("Called afterChunk() : {}", context);
    }

    @Override
    public void beforeChunk(ChunkContext context) {
        log.info("Called beforeChunk() : {}", context);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        log.info("Called afterChunkError() : {}", context);
    }
}
