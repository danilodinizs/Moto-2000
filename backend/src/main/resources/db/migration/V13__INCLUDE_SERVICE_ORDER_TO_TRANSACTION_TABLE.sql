ALTER TABLE transaction_tb
ADD COLUMN service_order_id UUID REFERENCES service_order_tb(id);