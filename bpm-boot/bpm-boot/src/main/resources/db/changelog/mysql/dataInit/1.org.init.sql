
-- 组织
INSERT INTO `org_group` (`id_`, `name_`, `parent_id_`, `sn_`, `code_`, `type_`, `desc_`, `create_time_`, `create_by_`, `update_time_`, `update_by_`, `path_`) VALUES ('1', '敏捷工作流程', '0', '1', 'htrj', '0', NULL, NULL, NULL, '2018-12-30 20:57:27', '1', NULL);
INSERT INTO `org_group` (`id_`, `name_`, `parent_id_`, `sn_`, `code_`, `type_`, `desc_`, `create_time_`, `create_by_`, `update_time_`, `update_by_`, `path_`) VALUES ('20000000280001', '科技部', '20000003450003', '3', 'kjb', '3', NULL, NULL, NULL, '2018-12-30 20:57:43', '1', NULL);
INSERT INTO `org_group` (`id_`, `name_`, `parent_id_`, `sn_`, `code_`, `type_`, `desc_`, `create_time_`, `create_by_`, `update_time_`, `update_by_`, `path_`) VALUES ('20000000280002', '销售部', '20000003450003', '2', 'xsb', '3', NULL, NULL, NULL, '2018-12-30 20:57:34', '1', NULL);
INSERT INTO `org_group` (`id_`, `name_`, `parent_id_`, `sn_`, `code_`, `type_`, `desc_`, `create_time_`, `create_by_`, `update_time_`, `update_by_`, `path_`) VALUES ('20000003450003', '上海分公司', '1', NULL, '711', '1', NULL, NULL, NULL, '2018-12-30 20:57:17', '1', NULL);
-- 用户
INSERT INTO `org_user` (`id_`, `fullname_`, `account_`, `password_`, `email_`, `mobile_`, `weixin_`, `address_`, `photo_`, `sex_`, `from_`, `status_`, `create_time_`, `create_by_`, `update_by_`, `update_time_`) VALUES ('1', '系统管理员', 'admin', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=', 'bpm@live.com', '1390000', 'test', 'test', NULL, '未知', 'system', '1', NULL, NULL, NULL, NULL);