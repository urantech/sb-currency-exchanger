DROP TABLE IF EXISTS exchange_rates;
DROP TABLE IF EXISTS currencies;

CREATE TABLE currencies (
                            id SERIAL PRIMARY KEY,
                            code VARCHAR(10) NOT NULL UNIQUE,
                            full_name VARCHAR(100) NOT NULL,
                            sign VARCHAR(5) NOT NULL
);

CREATE TABLE exchange_rates (
                                id SERIAL PRIMARY KEY,
                                base_currency_id INTEGER NOT NULL,
                                target_currency_id INTEGER NOT NULL,
                                rate DECIMAL(10, 4) NOT NULL,
                                CONSTRAINT unique_currency_pair UNIQUE (base_currency_id, target_currency_id),
                                CONSTRAINT fk_base_currency FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
                                CONSTRAINT fk_target_currency FOREIGN KEY (target_currency_id) REFERENCES currencies(id)
);