package cn.cidea.module.pm.dal.es;

import cn.cidea.module.pm.PmServerApplication;
import cn.cidea.module.pm.dataobject.dto.PmProductDTO;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author: CIdea
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PmServerApplication.class)
public class PmProductElasticsearchRepositoryTest {

    // @Autowired
    // private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private PmProductElasticsearchRepository rep;


    @Test // 插入一条记录
    public void testInsert() {
        PmProductDTO product = new PmProductDTO();
        product.setId(1L); // 一般 ES 的 ID 编号，使用 DB 数据对应的编号。这里，先写死
        product.setName("芋道源码");
        product.setCode("愿半生编码，如一生老友");
        rep.save(product);
    }

    // 这里要注意，如果使用 save 方法来更新的话，必须是全量字段，否则其它字段会被覆盖。
    // 所以，这里仅仅是作为一个示例。
    @Test // 更新一条记录
    public void testUpdate() {
        PmProductDTO product = new PmProductDTO();
        product.setId(1L);
        product.setMfrId(22L);
        rep.save(product);
    }

    @Test // 根据 ID 编号，删除一条记录
    public void testDelete() {
        rep.deleteById(1L);
    }

    @Test // 根据 ID 编号，查询一条记录
    public void testSelectById() {
        Optional<PmProductDTO> productDTO = rep.findById(1L);
        System.out.println(productDTO.isPresent());
    }

    @Test // 根据 ID 编号数组，查询多条记录
    public void testSelectByIds() {
        Iterable<PmProductDTO> list = rep.findAllById(Arrays.asList(1L, 4L));
        list.forEach(System.out::println);
    }

    // @Autowired
    // ElasticsearchClient client;
    // @Test
    // public void test() throws IOException {
    //     SearchResponse<JSONObject> search = client.search(builder -> builder.index("index").query(q -> q.match(m -> m.field("name").query(FieldValue.of("san")))), JSONObject.class);
    //     return;
    // }
}