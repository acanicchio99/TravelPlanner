-- Tabella SEARCH_HISTORY (per tracciare le ricerche degli utenti)
CREATE TABLE search_history (
    id BIGSERIAL PRIMARY KEY,
    search_term VARCHAR(255) NOT NULL,
    searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    results_count INTEGER
);

-- Indice per migliorare le performance
CREATE INDEX idx_search_history_searched_at ON search_history(searched_at);
CREATE INDEX idx_search_history_search_term ON search_history(search_term);
