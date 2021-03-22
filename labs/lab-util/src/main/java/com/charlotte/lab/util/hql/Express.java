package com.charlotte.lab.util.hql;

import org.apache.commons.lang3.StringUtils;

public abstract class Express implements IExpress {
    protected String left;

    static class DefaultExpress extends Express {
        protected String opt;
        protected String right;

        private DefaultExpress(String left, String opt, String right) {
            super.left = left;
            this.opt = opt;
            this.right = right;
        }

        @Override
        public StringBuilder generateExpress() {
            return new StringBuilder(left).append(" ").append(opt).append(right).append(" ");
        }
    }

    static class IsNullExpress extends Express {

        private IsNullExpress(String left) {
            super.left = left;
        }

        @Override
        public StringBuilder generateExpress() {
            return new StringBuilder(left).append(" IS NULL ");
        }
    }

    static class InExpress extends Express {
        protected String[] right;

        private InExpress(String left, String... right) {
            super.left = left;
            this.right = right;
        }

        @Override
        public StringBuilder generateExpress() {
            return new StringBuilder(left).append(" IN (").append(StringUtils.join(right, ",")).append(") ");
        }
    }

    static class LogicExpress extends Express {
        protected IExpress left;
        protected String opt;
        protected IExpress right;
        protected boolean prior;

        private LogicExpress(IExpress left, String opt, IExpress right) {
            this.left = left;
            this.opt = opt;
            this.right = right;
        }

        @Override
        public StringBuilder generateExpress() {
            StringBuilder builder = new StringBuilder(left.generate()).append(" ").append(opt).append(" ");
            if (!prior) {
                builder.append("(");
            }
            builder.append(right.generate());
            if (!prior) {
                builder.append(")");
            }
            builder.append(" ");
            return builder;
        }
    }

    public static Express equals(String name, String value) {
        return new DefaultExpress(name, "=", value);
    }

    public static Express notEquals(String name, String value) {
        return new DefaultExpress(name, "!=", value);
    }

    public static Express in(String name, String... value) {
        return new InExpress(name, value);
    }

    public static Express isNull(String name) {
        return new IsNullExpress(name);
    }

    public Express and(IExpress express) {
        return new LogicExpress(this, "AND", express);
    }

    /**
     * 优先and
     *
     * @param express
     * @return
     */
    public Express andPrior(IExpress express) {
        LogicExpress and = new LogicExpress(this, "AND", express);
        and.prior = true;
        return and;
    }

    public Express or(IExpress express) {
        return new LogicExpress(this, "OR", express);
    }

    /**
     * 优先or
     *
     * @param express
     * @return
     */
    public Express orPrior(IExpress express) {
        LogicExpress or = new LogicExpress(this, "OR", express);
        or.prior = true;
        return or;
    }

    @Override
    public StringBuilder generate() {
        return generateExpress();
    }

    public abstract StringBuilder generateExpress();

    //    @Override
//    public StringBuilder generate() {
//        Object left = parent.getLeft();
//        String opt = parent.getOpt();
//        Object right = parent.getRight();
//        StringBuilder leftBuilder;
//        StringBuilder rightBuilder;
//        if (left instanceof Express) {
//            leftBuilder = generateWhere((Express) left);
//        } else {
//            if(left instanceof String){
//                leftBuilder = new StringBuilder(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, left.toString()));
//            } else {
//                throw new RuntimeException("illege");
//            }
//        }
//
//        if(right == null && "IS".equals(opt)){
//            // is null
//            // todo
//            rightBuilder = new StringBuilder("NULL ");
//        } else if(right instanceof Express && ("AND".equals(opt) || "OR".equals(opt))){
//            // and/or
//            rightBuilder = generateWhere((Express) right);
//        } else if(right instanceof Hql && "IN".equals(opt)){
//            // in (select)
//            // todo
//            Hql rightHql = (Hql) right;
//            rightBuilder = new StringBuilder("(").append(rightHql.generate()).append(")");
//            parameterMap.putAll(rightHql.getParameterMap());
//        } else if(right instanceof List && "IN".equals(opt)){
//            // in ()
//            List<String> rightList = (List<String>) right;
//            if(rightList.size() == 0){
//                throw new RuntimeException("illega");
//            }
//            rightBuilder = new StringBuilder("(");
//            for (String val : rightList) {
//                String name = left.toString() + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 4);
//                rightBuilder = rightBuilder.append(":").append(name);
//                parameterMap.put(name, val);
//                rightBuilder.append(",");
//            }
//            rightBuilder.deleteCharAt(rightBuilder.length() - 1);
//            rightBuilder.append(")");
//        } else if(right instanceof String && ("=".equals(opt) || "!=".equals(opt)
//                || ">".equals(opt) || ">=".equals(opt)
//                || "<".equals(opt) || "<=".equals(opt))){
//            // compare
//            String name = left.toString() + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 4);
//            rightBuilder = new StringBuilder(":").append(name);
//            parameterMap.put(name, right.toString());
//        } else {
//            throw new RuntimeException("illege");
//        }
//        if(parent.isPrior()){
//            rightBuilder = rightBuilder.insert(0, "(").append(")");
//        }
//        StringBuilder builder = leftBuilder.append(" ").append(opt).append(" ").append(rightBuilder);
//        return builder;
//    }
}
