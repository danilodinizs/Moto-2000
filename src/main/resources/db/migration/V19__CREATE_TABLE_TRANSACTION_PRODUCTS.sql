CREATE TABLE IF NOT EXISTS transaction_products (
    transaction_id UUID NOT NULL,
    product_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (transaction_id, product_id),
    CONSTRAINT fk_transaction FOREIGN KEY (transaction_id) REFERENCES transaction_tb(id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product_tb(id) ON DELETE CASCADE
);