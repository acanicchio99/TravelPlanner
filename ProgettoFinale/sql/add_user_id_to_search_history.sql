-- Aggiunta colonna user_id a search_history e vincolo di chiave esterna
ALTER TABLE search_history 
ADD COLUMN user_id BIGINT NOT NULL DEFAULT 1;

-- Aggiunta vincolo di chiave esterna verso users
ALTER TABLE search_history 
ADD CONSTRAINT fk_search_history_user_id 
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Rimozione del DEFAULT dopo aver garantito che tutti i record abbiano un user_id valido
ALTER TABLE search_history 
ALTER COLUMN user_id DROP DEFAULT;

-- Creazione indice per migliorare le performance
CREATE INDEX idx_search_history_user_id ON search_history(user_id);
