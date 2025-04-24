CREATE TABLE transaction_tb (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_products INTEGER NOT NULL,
    total_price NUMERIC(19, 2) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    transaction_status VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    note TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    product_id UUID REFERENCES product_tb(id),
    client_id UUID REFERENCES client_tb(id),
    supplier_id UUID REFERENCES supplier_tb(id),
    CONSTRAINT positive_total_products CHECK (total_products > 0),
    CONSTRAINT positive_total_price CHECK (total_price > 0)
);
