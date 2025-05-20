ALTER TABLE service_order_tb
ADD COLUMN transaction_id UUID REFERENCES transaction_tb(id);