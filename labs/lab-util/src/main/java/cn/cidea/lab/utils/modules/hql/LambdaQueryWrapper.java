package cn.cidea.lab.utils.modules.hql;


import cn.cidea.lab.utils.common.dataobject.entity.Dept;
import cn.cidea.lab.utils.common.dataobject.entity.User;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class LambdaQueryWrapper<T> extends AbstractQueryWrapper<T, SFunction<T, ?>, LambdaQueryWrapper<T>> {

    protected List<SFunction<T, ?>> seletcList;

    public LambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            seletcList = Arrays.asList(columns);
        }
        return typedThis;
    }

    private <F> LambdaQueryWrapper<T> left(SFunction<T, ?> leftWhere, SFunction<F, ?> rightWhere, SFunction<LambdaQueryWrapper<F>, ?> fun) {

        return typedThis;
    }

    public static void main(String[] args) {
        new LambdaQueryWrapper<User>()
                .select(User::getId, User::getName)
                .eq(User::getDeleted, "0")
                .left(User::getDeptId, Dept::getId,
                        deptLambdaQueryWrapper -> deptLambdaQueryWrapper
                                .select(Dept::getName).eq(Dept::getDeleted, "0"))
        ;
    }

}
