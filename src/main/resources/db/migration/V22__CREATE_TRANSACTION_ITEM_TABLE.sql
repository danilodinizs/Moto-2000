CREATE TABLE transaction_item_tb (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    quantity INTEGER NOT NULL,
    product_id UUID NOT NULL REFERENCES product_tb(id),
    transaction_id UUID NOT NULL REFERENCES transaction_tb(id),
    CONSTRAINT positive_quantity CHECK (quantity > 0)
);