package com.imooc.core.repository;

import com.imooc.core.commonbean.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {
    UserInfo getUserInfoByPhone(String phone);
}
