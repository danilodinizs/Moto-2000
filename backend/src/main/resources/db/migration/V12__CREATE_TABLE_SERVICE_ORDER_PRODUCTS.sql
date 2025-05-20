CREATE TABLE service_order_products_tb (
    service_order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    PRIMARY KEY (service_order_id, product_id),
    CONSTRAINT fk_service_order
        FOREIGN KEY (service_order_id)
        REFERENCES service_order_tb (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
        REFERENCES product_tb (id)
        ON DELETE CASCADE
);
