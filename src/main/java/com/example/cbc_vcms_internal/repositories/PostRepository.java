package com.example.cbc_vcms_internal.repositories;

import com.example.cbc_vcms_internal.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
