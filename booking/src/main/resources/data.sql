INSERT INTO hotels (id, name, location,description)
VALUES
(1, 'Grand Nile Hotel', 'Cairo' , 'Description'),
(2, 'Sea View Resort', 'Alexandria', 'Description');

INSERT INTO rooms (room_number, room_type, price, availability, hotel_id)
VALUES
(101, 'SINGLE', 1000, true, 1),
(102, 'DOUBLE', 1500, true, 1),
(201, 'SUITE', 2500, false, 2);

INSERT INTO users (name, email, password, role, phone_number)
VALUES
('Karim Bassel', 'karim@mail.com', '123456', 'GUEST', '01012345678'),
('Admin User', 'admin@mail.com', 'admin123', 'ADMIN', '01198765432');


INSERT INTO bookings (
    id,
    base_price,
    check_in,
    check_out,
    created_at,
    status,
    user_id,
    room_id
)
VALUES
(
    10,
    2400,
    '2025-01-10',
    '2025-01-12',
    NOW(),
    'PENDING',
    1,
    1
),
(
    11,
    3000,
    '2025-01-15',
    '2025-01-17',
    NOW(),
    'CONFIRMED',
    1,
    2
);


INSERT INTO payments (
    id,
    booking_id,
    amount_to_be_paid,
    payment_date,
    payment_status,
    payment_method
)
VALUES
(
    1,
    10,
    3000,
    NOW(),
    'PENDING',
    'CASH'
);





INSERT INTO reviews (user_id, hotel_id, review)
VALUES
(1, 1, 4.5),
(1, 2, 4.0),
(2, 1, 5.0);
