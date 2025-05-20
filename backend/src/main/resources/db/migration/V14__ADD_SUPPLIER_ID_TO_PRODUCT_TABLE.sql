ALTER TABLE product_tb
ADD COLUMN supplier_id UUID REFERENCES supplier_tb(id);