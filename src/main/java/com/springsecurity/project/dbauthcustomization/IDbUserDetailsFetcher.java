package com.springsecurity.project.dbauthcustomization;

import java.util.Optional;


public interface IDbUserDetailsFetcher {
	Optional<DbUserDetails> findUserDetailsByUsername(String username);
}
