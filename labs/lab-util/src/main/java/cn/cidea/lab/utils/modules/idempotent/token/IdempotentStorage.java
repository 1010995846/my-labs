package cn.cidea.lab.utils.modules.idempotent.token;

/**
 * @author: CIdea
 */
public interface IdempotentStorage {

    void save(String id);

    boolean delete(String id);
}
