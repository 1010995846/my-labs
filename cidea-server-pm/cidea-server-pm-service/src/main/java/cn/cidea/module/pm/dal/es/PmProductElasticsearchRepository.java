package cn.cidea.module.pm.dal.es;

import cn.cidea.module.pm.dataobject.dto.PmProductDTO;
import cn.cidea.module.pm.dataobject.entity.PmProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author: CIdea
 */
public interface PmProductElasticsearchRepository extends ElasticsearchRepository<PmProductDTO, Long> {
}
