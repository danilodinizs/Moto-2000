CREATE TABLE product_tb (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(100),
    price NUMERIC(19, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    description TEXT NOT NULL,
    expiry_date TIMESTAMP,
    image_url VARCHAR(512),
    category_id UUID REFERENCES category_tb(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT positive_price CHECK (price > 0),
    CONSTRAINT non_negative_stock CHECK (stock_quantity >= 0)
);