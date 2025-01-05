ALTER TABLE `seven_signs_festival`
ADD COLUMN `members_reward_flags` varchar(255) NOT NULL DEFAULT '' AFTER `members`;

ALTER TABLE `seven_signs_status`
DROP COLUMN `festival_cycle`;