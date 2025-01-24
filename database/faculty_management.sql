/*
 Navicat Premium Data Transfer

 Source Server         : teacher_managemen
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : faculty_management

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 03/01/2025 03:21:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_class
-- ----------------------------
DROP TABLE IF EXISTS `t_class`;
CREATE TABLE `t_class`  (
  `class_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `department_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`class_id`) USING BTREE,
  INDEX `department_id`(`department_id` ASC) USING BTREE,
  CONSTRAINT `t_class_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `t_department` (`dept_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_class
-- ----------------------------
INSERT INTO `t_class` VALUES ('CE2401', '土木24-1', 'CE01');
INSERT INTO `t_class` VALUES ('COM2401', '计科24-1', 'COM01');
INSERT INTO `t_class` VALUES ('COM2402', '计科24-2', 'COM01');
INSERT INTO `t_class` VALUES ('COM2403', '计科24-3', 'COM01');
INSERT INTO `t_class` VALUES ('ECE2401', '电信24-1', 'ECE01');
INSERT INTO `t_class` VALUES ('ECE2402', '电信24-2', 'ECE01');
INSERT INTO `t_class` VALUES ('MSS2401', '马院24-1', 'MSS01');
INSERT INTO `t_class` VALUES ('MSS2402', '马院24-2', 'MSS01');

-- ----------------------------
-- Table structure for t_course
-- ----------------------------
DROP TABLE IF EXISTS `t_course`;
CREATE TABLE `t_course`  (
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `course_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dept_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `credits` decimal(3, 1) NOT NULL,
  `total_hours` int NOT NULL,
  `course_nature` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `assessment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`course_id`) USING BTREE,
  INDEX `dept_id`(`dept_id` ASC) USING BTREE,
  CONSTRAINT `t_course_ibfk_1` FOREIGN KEY (`dept_id`) REFERENCES `t_department` (`dept_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_course
-- ----------------------------
INSERT INTO `t_course` VALUES ('CE302', '工程地质', 'CE01', 3.5, 48, '必修', '实验');
INSERT INTO `t_course` VALUES ('CE303', '建筑材料', 'CE01', 2.5, 40, '选修', '实验');
INSERT INTO `t_course` VALUES ('CSE101', '程序设计基础', 'COM01', 3.0, 48, '必修', '笔试');
INSERT INTO `t_course` VALUES ('CSE102', '计算机网络', 'COM01', 3.5, 60, '选修', '实验');
INSERT INTO `t_course` VALUES ('CSE103', '操作系统', 'COM01', 4.0, 64, '必修', '笔试');
INSERT INTO `t_course` VALUES ('CSE104', '数据结构', 'COM01', 3.0, 48, '必修', '实验');
INSERT INTO `t_course` VALUES ('ECE201', '电路分析', 'ECE01', 3.0, 48, '必修', '笔试');
INSERT INTO `t_course` VALUES ('ECE202', '信号与系统', 'ECE01', 4.0, 64, '必修', '笔试');
INSERT INTO `t_course` VALUES ('ECE203', '通信原理', 'ECE01', 3.5, 56, '选修', '实验');
INSERT INTO `t_course` VALUES ('ECE205', '数字信号处理', 'ECE01', 3.0, 48, '选修', '论文');
INSERT INTO `t_course` VALUES ('MSS401', '马克思主义基本原理', 'MSS01', 2.0, 32, '必修', '笔试');
INSERT INTO `t_course` VALUES ('MSS402', '思想道德修养与法律基础', 'MSS01', 3.0, 48, '必修', '论文');

-- ----------------------------
-- Table structure for t_department
-- ----------------------------
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department`  (
  `dept_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `num_of_people` int NULL DEFAULT 0,
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_department
-- ----------------------------
INSERT INTO `t_department` VALUES ('CE01', '土木工程学院', 0);
INSERT INTO `t_department` VALUES ('COM01', '计算机科学与技术学院', 1);
INSERT INTO `t_department` VALUES ('ECE01', '电子信息工程学院', 0);
INSERT INTO `t_department` VALUES ('MGT01', '管理办', 1);
INSERT INTO `t_department` VALUES ('MSS01', '马克思主义学院', 1);

-- ----------------------------
-- Table structure for t_faculty
-- ----------------------------
DROP TABLE IF EXISTS `t_faculty`;
CREATE TABLE `t_faculty`  (
  `faculty_id` int NOT NULL AUTO_INCREMENT,
  `faculty_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `department_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_id` int NOT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` tinyint NOT NULL DEFAULT 1,
  `hire_date` date NOT NULL,
  `leave_date` date NULL DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`faculty_id`) USING BTREE,
  UNIQUE INDEX `id_number`(`id_number` ASC) USING BTREE,
  INDEX `department_id`(`department_id` ASC) USING BTREE,
  INDEX `role_id`(`role_id` ASC) USING BTREE,
  CONSTRAINT `t_faculty_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `t_department` (`dept_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_faculty_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1005 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_faculty
-- ----------------------------
INSERT INTO `t_faculty` VALUES (1001, '子鱼', 'MGT01', 1, '13812345678', '110101199001011234', 1, '2024-12-31', NULL, '88888888');
INSERT INTO `t_faculty` VALUES (1002, '军豪', 'MSS01', 2, '13987654321', '220202198512345678', 1, '2024-12-31', NULL, '88888888');
INSERT INTO `t_faculty` VALUES (1003, '刚子', 'COM01', 3, '13765432109', '330303197612345123', 1, '2024-12-31', NULL, '88888888');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `base_salary` decimal(10, 2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, '超级管理员', 10000.00);
INSERT INTO `t_role` VALUES (2, '部门管理员', 8000.00);
INSERT INTO `t_role` VALUES (3, '普通职工', 5000.00);

-- ----------------------------
-- Table structure for t_salary
-- ----------------------------
DROP TABLE IF EXISTS `t_salary`;
CREATE TABLE `t_salary`  (
  `salary_id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL,
  `salary_date` date NOT NULL,
  `base_salary` decimal(10, 2) NOT NULL,
  `total_hours` int NOT NULL,
  `total_salary` decimal(10, 2) NOT NULL,
  PRIMARY KEY (`salary_id`) USING BTREE,
  INDEX `employee_id`(`employee_id` ASC) USING BTREE,
  CONSTRAINT `t_salary_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `t_faculty` (`faculty_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_salary
-- ----------------------------

-- ----------------------------
-- Table structure for t_schedule
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule`;
CREATE TABLE `t_schedule`  (
  `schedule_id` int NOT NULL AUTO_INCREMENT,
  `course_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `start_week` tinyint NOT NULL,
  `end_week` tinyint NOT NULL,
  `week_day` tinyint NOT NULL,
  `class_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `faculty_id` int NOT NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`schedule_id`) USING BTREE,
  INDEX `fk_course`(`course_id` ASC) USING BTREE,
  INDEX `fk_faculty`(`faculty_id` ASC) USING BTREE,
  CONSTRAINT `fk_course` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`course_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_faculty` FOREIGN KEY (`faculty_id`) REFERENCES `t_faculty` (`faculty_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程安排表，记录课程的时间和地点信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_schedule
-- ----------------------------
INSERT INTO `t_schedule` VALUES (27, 'CE302', 1, 15, 2, '5', 1003, 'B204');
INSERT INTO `t_schedule` VALUES (28, 'CSE101', 1, 12, 4, '5', 1003, 'B208');

-- ----------------------------
-- Table structure for t_schedule_target
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_target`;
CREATE TABLE `t_schedule_target`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `schedule_id` int NOT NULL,
  `target_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `target_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_schedule`(`schedule_id` ASC) USING BTREE,
  CONSTRAINT `fk_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `t_schedule` (`schedule_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程安排与上课对象的关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_schedule_target
-- ----------------------------
INSERT INTO `t_schedule_target` VALUES (45, 27, 'student', '2230090405');
INSERT INTO `t_schedule_target` VALUES (46, 27, 'student', '25MS0001');
INSERT INTO `t_schedule_target` VALUES (47, 28, 'student', '25MS0001');

-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student`  (
  `student_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `student_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `enrollment_year` int NOT NULL,
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `id_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` tinyint NOT NULL DEFAULT 1,
  `department_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `class_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`student_id`) USING BTREE,
  UNIQUE INDEX `id_number`(`id_number` ASC) USING BTREE,
  INDEX `department_id`(`department_id` ASC) USING BTREE,
  INDEX `class_id`(`class_id` ASC) USING BTREE,
  CONSTRAINT `t_student_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `t_department` (`dept_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_student_ibfk_2` FOREIGN KEY (`class_id`) REFERENCES `t_class` (`class_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_student
-- ----------------------------
INSERT INTO `t_student` VALUES ('2230090405', '董军豪', 'M', 2025, '15806422235', '371321200405011111', 1, 'CE01', 'CE2401');
INSERT INTO `t_student` VALUES ('25MS0001', 'djh', 'M', 2025, '15806422237', '371321200405011118', 1, 'MSS01', 'MSS2401');

SET FOREIGN_KEY_CHECKS = 1;
