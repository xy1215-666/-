-- Run after creating/cancelling an order. The exact order id is supplied by the test run.
SELECT id, order_no, food_id, quantity, status, total_amount
FROM food_orders
WHERE id = :order_id;

SELECT id, name, stock, available
FROM food_items
WHERE id = :food_id;

-- A cancellation should restore the quantity exactly once.
-- Expected: current_stock = stock_before_order + cancelled_quantity.

