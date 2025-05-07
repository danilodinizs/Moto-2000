CREATE TABLE service_order_tb (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    duration VARCHAR(50),
    labor_cost DECIMAL(19,2),
    created_at TIMESTAMP
);