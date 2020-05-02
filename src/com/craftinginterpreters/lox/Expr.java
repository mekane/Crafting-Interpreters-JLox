package com.craftinginterpreters.lox;

abstract class Expr {
    abstract <R> R accept(Visitor<R> visitor);

    static class Literal extends Expr {
        final Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    static class Unary extends Expr {
        final Token operator;
        final Expr right;

        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    static class Binary extends Expr {
        final Expr left;
        final Token operator;
        final Expr right;

        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    static class Grouping extends Expr {
        final Expr expression;

        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }



    interface Visitor<R> {
        //R visitAssignExpr(Assign expr);
        R visitBinaryExpr(Binary expr);
        //R visitCallExpr(Call expr);
        //R visitGetExpr(Get expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
//        R visitLogicalExpr(Logical expr);
//        R visitSetExpr(Set expr);
//        R visitSuperExpr(Super expr);
//        R visitThisExpr(This expr);
        R visitUnaryExpr(Unary expr);
//        R visitVariableExpr(Variable expr);
    }
}