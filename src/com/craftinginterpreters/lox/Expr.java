package com.craftinginterpreters.lox;

public abstract class Expr {
    abstract <R> R accept(Visitor<R> visitor);

    public static class Literal extends Expr {
        public final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    public static class Unary extends Expr {
        public final Token operator;
        public final Expr right;

        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    public static class Binary extends Expr {
        public final Expr left;
        public final Token operator;
        public final Expr right;

        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static class Ternary extends Expr {
        public final Expr condition;
        public final Expr left;
        public final Expr right;

        public Ternary(Expr condition, Expr left, Expr right) {
            this.condition = condition;
            this.left = left;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitTernaryExpr(this);
        }
    }

    public static class Grouping extends Expr {
        public final Expr expression;

        public Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    public static class Variable extends Expr {
        Variable(Token name) {
            this.name = name;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }

        final Token name;
    }

    interface Visitor<R> {
        //R visitAssignExpr(Assign expr);
        R visitBinaryExpr(Binary expr);

        R visitTernaryExpr(Ternary expr);

        //R visitCallExpr(Call expr);
        //R visitGetExpr(Get expr);
        R visitGroupingExpr(Grouping expr);

        R visitLiteralExpr(Literal expr);

        //        R visitLogicalExpr(Logical expr);
//        R visitSetExpr(Set expr);
//        R visitSuperExpr(Super expr);
//        R visitThisExpr(This expr);
        R visitUnaryExpr(Unary expr);

        R visitVariableExpr(Variable expr);
    }
}