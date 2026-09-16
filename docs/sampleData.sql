-- 0. Create Tables Schema
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS venue (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    stars INT NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS event (
    id UUID PRIMARY KEY,
    venue_id UUID NOT NULL REFERENCES venue(id),
    name VARCHAR(255) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    ticket_price NUMERIC(38, 2) NOT NULL,
    seat_capacity BIGINT NOT NULL,
    organizer_id UUID NOT NULL REFERENCES users(id),
    description VARCHAR(255),
    theme VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS seat (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES event(id) ON DELETE CASCADE,
    seat_number VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES event(id),
    customer_id UUID NOT NULL REFERENCES users(id),
    status VARCHAR(50) NOT NULL,
    total_price NUMERIC(38, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation_item (
    id UUID PRIMARY KEY,
    reservation_id UUID NOT NULL REFERENCES reservation(id) ON DELETE CASCADE,
    seat_id UUID NOT NULL REFERENCES seat(id),
    price NUMERIC(38, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS payment (
    id UUID PRIMARY KEY,
    reservation_id UUID NOT NULL UNIQUE REFERENCES reservation(id) ON DELETE CASCADE,
    amount NUMERIC(38, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payment_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id UUID PRIMARY KEY,
    token_hash VARCHAR(255),
    user_id UUID REFERENCES users(id),
    expiration_date TIMESTAMP
);

-- 1. Insert Users (Independent)
INSERT INTO users (creation_date, id, email, name, password, role, username)
VALUES 
    ('2026-09-13 17:51:50.252996', '123629ae-3e7c-46d6-b562-6d49131215e1', 'andrew@joe.com', 'andrew joe', '$2a$12$4QYbI.NtbarSK7zrskdrB.chqYHQz1mJFqUiwptLiffGWzwmfCrc6', 'ORGANIZER', 'andrew'),
    ('2026-09-13 17:51:50.252996', '223629ae-3e7c-46d6-b562-6d49131215e1', 'jack@joe.com', 'jack joe', '$2a$12$4QYbI.NtbarSK7zrskdrB.chqYHQz1mJFqUiwptLiffGWzwmfCrc6', 'ORGANIZER', 'jack'),
    ('2026-09-13 17:51:50.252996', '223629ae-3e7c-46d6-b562-6d49131215e7', 'joe@joe.com', 'joe joe', '$2a$12$4QYbI.NtbarSK7zrskdrB.chqYHQz1mJFqUiwptLiffGWzwmfCrc6', 'ADMIN', 'joe'),
    ('2026-09-13 18:50:38.105706', '48d65f32-eee1-427a-92c0-7ee2fc6b89f2', 'jame@smith.com', 'jane smith', '$2a$12$HDFlvNgfFdEtfZHec95YvufNhkSNgSgkZQMn3XCMYxduFoUyGvpVS', 'CUSTOMER', 'janesmith')
ON CONFLICT (id) DO NOTHING;

-- 2. Insert Venues (Independent)
INSERT INTO venue (id, stars, description, location, name)
VALUES 
    ('04820910-8686-4029-9eb3-af6d79e9f379', 2, 'A legendary local dive bar known for its unpretentious atmosphere, affordable drink specials, vintage jukebox, classic billiards tables, and late-night pub grub.', '50 Back Alley, Springfield', 'Moe''s Tavern'),
    ('23e58a08-24d4-4a85-93ec-1a43cf6dc679', 4, 'A vibrant, retro-themed bowling alley featuring 24 polished lanes, a state-of-the-art modern arcade, neon lighting, and a full-service bar serving craft cocktails.', '250 Strike Way, Chicago, IL', 'Neon Lanes'),
    ('735b466c-a553-4471-9cfc-364390b26e76', 4, 'An intimate, subterranean live music venue dedicated to showcasing emerging indie rock bands, featuring top-tier acoustics, a spacious dance floor, and a lively atmosphere.', '88 Bass Street, Austin, TX', 'The Underground'),
    ('770e8c15-2492-4040-b0b7-927fa9c6b99e', 5, 'A sprawling 50-acre botanical garden featuring exotic climate-controlled conservatories, peaceful walking trails, seasonal flower exhibits, and a large butterfly pavilion.', '400 Nature Trail, Denver, CO', 'Oasis Botanical Gardens'),
    ('876f545f-2a80-4dda-a207-cfd24557fd72', 5, 'An upscale fine dining seafood restaurant perched on the pier, offering panoramic ocean views, a curated wine list, and a daily rotating menu of locally caught fish.', '123 Pier Ave, Ocean City, MD', 'The Rusty Anchor'),
    ('9833e692-e557-4577-bfd8-61130dbf0060', 3, 'A warm and cozy neighborhood coffee shop offering ethically sourced artisanal espresso, complimentary high-speed wifi, plush seating, and daily fresh-baked goods.', '789 Main St, Seattle, WA', 'Daily Grind Cafe'),
    ('c4abf48c-673d-49c4-abee-8d115619011a', 5, 'A beautifully restored 1920s historic theater hosting world-class Broadway shows, award-winning musicals, and exclusive seasonal performances in an opulent setting.', '100 Broadway, New York, NY', 'The Grand Theater')
ON CONFLICT (id) DO NOTHING;

-- 3. Insert Events (Depends on venue.id and users.id)
INSERT INTO event (ticket_price, end_time, seat_capacity, start_time, id, organizer_id, venue_id, description, name, theme)
VALUES 
    (25.00, '2026-10-22 23:45:00', 250, '2026-10-22 19:30:00', '0b9d33c5-d625-4c0f-9224-81d2c7476a2a', '123629ae-3e7c-46d6-b562-6d49131215e1', '735b466c-a553-4471-9cfc-364390b26e76', 'Showcase of three emerging local indie bands with a lively dance floor and top-tier subterranean acoustics.', 'Underground Indie Rock Showcase', 'Live Music'),
    (45.00, '2026-11-05 16:00:00', 500, '2026-11-05 10:00:00', '0bb5ca06-fcf6-46ce-9e2f-29ebbeefe14f', '223629ae-3e7c-46d6-b562-6d49131215e1', '770e8c15-2492-4040-b0b7-927fa9c6b99e', 'A guided walking tour through exotic climate-controlled conservatories and the newly opened butterfly pavilion.', 'Autumn Conservatory Tour', 'Nature & Outdoors'),
    (35.50, '2026-10-18 01:00:00', 120, '2026-10-17 20:00:00', '2538c637-0862-4d31-a07a-6751974a9856', '223629ae-3e7c-46d6-b562-6d49131215e1', '23e58a08-24d4-4a85-93ec-1a43cf6dc679', 'Glow-in-the-dark bowling night complete with craft cocktail specials and high-score arcade challenges.', 'Midnight Neon Strike Party', 'Nightlife & Entertainment'),
    (15.00, '2026-10-15 23:30:00', 40, '2026-10-15 19:00:00', '53661f21-4996-4e54-80eb-6c62303b8c6f', '123629ae-3e7c-46d6-b562-6d49131215e1', '04820910-8686-4029-9eb3-af6d79e9f379', 'An unpretentious evening of 8-ball pool, featuring a bracket-style tournament and vintage jukebox hits.', 'Springfield Classic Billiards Tournament', 'Sports & Games'),
    (10.00, '2026-11-20 12:00:00', 30, '2026-11-20 08:00:00', '5be705df-530a-4ad9-af7a-f687a55c66e2', '223629ae-3e7c-46d6-b562-6d49131215e1', '9833e692-e557-4577-bfd8-61130dbf0060', 'Start your morning with freshly baked artisan goods, ethically sourced espresso, and live acoustic background music.', 'Acoustic Morning Brews', 'Community & Arts'),
    (150.00, '2026-11-12 22:00:00', 60, '2026-11-12 18:30:00', '680dfc2f-3f0d-4fba-a6b1-f276c5844d07', '123629ae-3e7c-46d6-b562-6d49131215e1', '876f545f-2a80-4dda-a207-cfd24557fd72', 'An exclusive 5-course seafood tasting menu paired with curated wines, featuring panoramic ocean sunset views.', 'Pier Sunset Seafood Tasting', 'Food & Dining'),
    (125.00, '2026-12-01 22:30:00', 1200, '2026-12-01 19:30:00', '88136c66-653f-4937-948a-b3220e70becc', '123629ae-3e7c-46d6-b562-6d49131215e1', 'c4abf48c-673d-49c4-abee-8d115619011a', 'Opening night of the award-winning Broadway musical revival, set within our opulent 1920s theater.', 'Winter Musical Gala Opening', 'Theater & Performing Arts')
ON CONFLICT (id) DO NOTHING;

-- 4. Generate Seats (Depends on event.id)
WITH event_capacities (event_id, capacity) AS (
    VALUES 
        ('0b9d33c5-d625-4c0f-9224-81d2c7476a2a'::uuid, 250),
        ('0bb5ca06-fcf6-46ce-9e2f-29ebbeefe14f'::uuid, 500),
        ('2538c637-0862-4d31-a07a-6751974a9856'::uuid, 120),
        ('53661f21-4996-4e54-80eb-6c62303b8c6f'::uuid, 40),
        ('5be705df-530a-4ad9-af7a-f687a55c66e2'::uuid, 30),
        ('680dfc2f-3f0d-4fba-a6b1-f276c5844d07'::uuid, 60),
        ('88136c66-653f-4937-948a-b3220e70becc'::uuid, 1200)
),
seat_generation AS (
    SELECT 
        ec.event_id,
        (s.num - 1) AS seat_index
    FROM event_capacities ec,
    LATERAL generate_series(1, ec.capacity) AS s(num)
)
INSERT INTO seat (version, event_id, id, seat_number, status)
SELECT 
    0 AS version, 
    event_id, 
    gen_random_uuid() AS id, 
    chr(65 + (seat_index / 50)::int) || ((seat_index % 50) + 1)::text AS seat_number, 
    'AVAILABLE' AS status
FROM seat_generation
ON CONFLICT (id) DO NOTHING;