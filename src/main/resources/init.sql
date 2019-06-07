-- Creating table structure


DROP TRIGGER IF EXISTS schedule_check ON schedule;
DROP TRIGGER IF EXISTS schedule_task_check ON schedule_task;

DROP FUNCTION IF EXISTS schedule_check;
DROP FUNCTION IF EXISTS schedule_task_check;

DROP TABLE IF EXISTS schedule_task;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	name VARCHAR(200),
	password VARCHAR(200),
	email VARCHAR(200) UNIQUE,
	role NUMERIC(1)	
);

CREATE TABLE schedule (
	id SERIAL PRIMARY KEY,
	name VARCHAR(200),
	users_id INT REFERENCES users(id),
	date DATE,
	max_days NUMERIC(1),
	public bool
);

CREATE TABLE task (
	id SERIAL PRIMARY KEY,
	user_id INT REFERENCES users(id),
	title VARCHAR(200),
	content TEXT
);

CREATE TABLE schedule_task (
	schedule_id INT REFERENCES schedule(id) ON DELETE CASCADE,
	task_id INT REFERENCES task(id) ON DELETE CASCADE,
	day NUMERIC(1),
	hour_start NUMERIC(2),
	hour_end NUMERIC(2),
	PRIMARY KEY (schedule_id, task_id)
);

CREATE INDEX schedule_date_index ON schedule(date);


-- Trigger functions

CREATE FUNCTION schedule_task_check() RETURNS trigger 
AS '
	BEGIN
		IF NEW.hour_start > 23 OR NEW.hour_start < 0 THEN
			RAISE EXCEPTION ''hour_start must be between 0 and 23'';
		END IF;
		IF NEW.hour_end > 24 OR NEW.hour_end < 1 THEN
			RAISE EXCEPTION ''hour_end must be between 1 and 24'';
		END IF;
		IF NEW.hour_end <= NEW.hour_start THEN
			RAISE EXCEPTION ''hour_end must be greater than hour_start'';
		END IF;
		IF NEW.day > (SELECT max_days FROM schedule WHERE NEW.schedule_id = schedule.id) THEN
			RAISE EXCEPTION ''Task day can''''t be greater than schedule max days'';
		END IF;
		IF (SELECT count(*) FROM schedule_task
			WHERE schedule_id = NEW.schedule_id
			AND day = NEW.day
			AND hour_start < NEW.hour_end
			AND hour_end > NEW.hour_start) > 0
			THEN RAISE EXCEPTION ''Overlapping hours!'';
		END IF;
		IF (SELECT user_id FROM task WHERE id = NEW.task_id) != (SELECT users_id FROM schedule WHERE id = NEW.schedule_id) THEN
			RAISE EXCEPTION ''Tasks cannot be attached to a schedule with different user id! '';
		END IF;
		RETURN NEW;
	END; '
LANGUAGE plpgsql;

CREATE TRIGGER schedule_task_check BEFORE INSERT OR UPDATE ON schedule_task
	FOR EACH ROW EXECUTE PROCEDURE schedule_task_check();

CREATE FUNCTION schedule_check() RETURNS trigger 
AS '
	BEGIN
		IF (SELECT count(*) FROM schedule
			WHERE users_id = NEW.users_id
			AND date < NEW.date + interval ''1 day'' * NEW.max_days
			AND date + interval ''1 day'' * max_days > NEW.date
			) > 0
			THEN RAISE EXCEPTION ''Overlapping schedule dates!'';
		END IF;
		RETURN NEW;
	END; '
LANGUAGE plpgsql;

CREATE TRIGGER schedule_check BEFORE INSERT ON schedule
	FOR EACH ROW EXECUTE PROCEDURE schedule_check();


--Filling tables with stock data

INSERT INTO users (email, password, name, role) VALUES
('a', '$2a$09$JJ4Tk7UjoP8Vi0bB3HtOP.skqm9jBSxFSNtrl1OIUJLucqMsQAJBS', 'Mr. A.', 1), --1
('b', '$2a$09$JJ4Tk7UjoP8Vi0bB3HtOP.skqm9jBSxFSNtrl1OIUJLucqMsQAJBS', 'Hector Brown jr.', 1),  --2
('janeklawovsky@codecool.com', '$2a$09$gwuETYP0flnIPAeDZ/X2BeSrz2bbo/Lv37VTe00Y2aoITflER/P3S', 'Jane Klawovsky', 0),  --3
('horvath@codecool.hu', '$2a$09$eqcuUZxN48NvDXamPiR33eQiphNLqBU8KQPkm.m9v3UclUEmUEQjm', 'Horváth Ödön', 0),  --4
('kucslubta@codecool.hu', '$2a$09$Xz6m5Z8Io6XWg9R1XZuP8.hGre0G8nwOmDZxFtT/hSKYZfhcCLEuC', 'Kucslubta Sándorné', 0),  --5
('piroska@piroska.hu', crypt('pirike123', gen_salt('bf', 9)), 'Piroska', 0) --6
;

INSERT INTO schedule (users_id, name, date, max_days, public) VALUES
(1, 'Experimentation week', '2019-01-15', 7, '1'),  --1
(1, 'Shakespeare art project', '2019-02-21', 7, '0'),  --2
(2, 'SCRUM Refactoring Tournament', '2019-02-21', 6, '0'),  --3
(2, 'Summer holidays', '2019-04-12', 5, '0'),  --4
(2, 'Refurbishing living room', '2019-05-10', 7, '0'),  --5
(3, 'Philosophy workshop', '2019-05-10', 7, '0'),  --6
(1, 'Schedule Master week #1', '2019-07-21', 5, '0'),  --7
(4, 'Schedule Master week #2', '2019-08-02', 6, '0'),  --8
(4, 'Schedule Master week #3', '2019-09-12', 5, '0'),  --9
(6, 'Dev Week', '2019-06-03', 5, '0')  --10
;


INSERT INTO task (user_id, title, content) VALUES
(1, 'Radiator building', 'Refurbishing my living room radiator'), --1
(1, 'Implementing skeleton code', 'Implementation of base skeleton code'),  --2
(1, 'Database init script', 'Creating an init script for db operations'),  --3
(1, 'Git tests', 'Testing git and resolving possible errors'),  --4
(1, 'Java module reconfiguration', 'Reconfiguring java module for web use'),  --5
(1, 'Parsing strings', 'Parsing file based content into sql strings'),  --6
(1, 'Implementing authorization', 'User priviliges and access creating'),  --7
(1, 'Doctor''s appointment', 'Regular checkup'),  --8
(2, 'Refactoring', 'Keeping old code up to date with new standards'), --9
(1, 'Cooking class', 'Learning to cook new exiting food'), --10
(1, 'Meditating', 'For inner peace'), --11
(6, 'Coding', 'Real work'), --12
(6, 'Debugging', 'Real work'), --13
(6, 'Refactoring', 'Surreal work'), --14
(6, 'Refactoring refactored code', 'Surreal work'), --15
(6, 'Refactoring refactored refactored code', 'Surreal work') --16
;

INSERT INTO schedule_task(schedule_id, task_id, day, hour_start, hour_end) VALUES
(7, 1, 5, 9, 12),
(7, 2, 2, 12, 15),
(7, 7, 3, 15, 16),
(1, 3, 2, 9, 10),
(1, 4, 4, 10, 12),
(1, 5, 3, 12, 14),
(1, 6, 2, 14, 16),
(1, 7, 3, 16, 18),
(1, 8, 1, 23, 24),
(10, 12, 1, 9, 18),
(10, 13, 2, 9, 18),
(10, 14, 3, 9, 18),
(10, 15, 4, 9, 18),
(10, 16, 5, 9, 18)
;


