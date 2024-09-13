package com.ictedu.springrestapi.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,String> {

	Optional<Users> findByUsername(String username);
	void deleteByUsername(String username);
}
