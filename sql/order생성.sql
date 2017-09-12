/*
SELECT * FROM bitsum.t_orders_buy;

select 1 from t_t
;
*/

delete from t_orders_buy where currency_pair = 'eth_krw';

insert into t_orders_buy  
SELECT 
    currency_pair,
    buy_seq,
    type,
    price - (buy_seq * 1000) as price,
    coin_amount,
    fiat_amount,
    buy_date,
    order_id,
    status
FROM
    (SELECT 
        'eth_krw' AS `currency_pair`,
            @RNUM:=@RNUM + 1 AS buy_seq,
            'limit' AS `type`,
            342000.00000 AS `price`,
            0.01000 AS `coin_amount`,
            0.01000 AS `fiat_amount`,
            NOW() AS `buy_date`,
            NULL `order_id`,
            'N' `status`
    FROM
        (SELECT 
        currency
    FROM
        t_ticker
    ORDER BY date ASC) a, (SELECT @RNUM:=0) b
    LIMIT 4) t
;    


delete from t_orders_sell where currency_pair = 'eth_krw';


insert into t_orders_sell    
SELECT `currency_pair`,
    `buy_seq` as `sell_seq`,
    `type`,
    price + (buy_seq * 1500) as price,
    `coin_amount`,
    buy_date as `sell_date`,
    `order_id`,
    `status`
FROM (SELECT 
        'eth_krw' AS `currency_pair`,
            @RNUM:=@RNUM + 1 AS buy_seq,
            'limit' AS `type`,
            342000.00000 AS `price`,
            0.01000 AS `coin_amount`,
            0.01000 AS `fiat_amount`,
            NOW() AS `buy_date`,
            NULL `order_id`,
            'N' `status`
    FROM
        (SELECT 
        currency
    FROM
        t_ticker
    ORDER BY date ASC) a, (SELECT @RNUM:=0) b
    LIMIT 4) t;
    