package io.github.changebooks.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * ES数仓
 *
 * @param <ID> 主键
 * @param <T>  ES实例
 * @author changebooks@qq.com
 */
@NoRepositoryBean
public interface EsRepository<ID, T extends EsEntity<ID>> extends ElasticsearchRepository<T, ID> {
}
