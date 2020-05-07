package com.craftinginterpreters.lox;

import java.util.List;

public abstract class Stmt {
    interface Visitor<R> {
        //R visitBlockStmt(Block stmt);
        //R visitClassStmt(Class stmt);
        R visitExpressionStmt(Expression stmt);

        //R visitFunctionStmt(Function stmt);
        //R visitIfStmt(If stmt);
        R visitPrintStmt(Print stmt);

        //R visitReturnStmt(Return stmt);
        R visitVarStmt(Var stmt);
        //R visitWhileStmt(While stmt);
    }

    public static class Expression extends Stmt {
        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }

        final Expr expression;
    }

    public static class Print extends Stmt {
        Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }

        final Expr expression;
    }

    static class Var extends Stmt {
        Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }

        final Token name;
        final Expr initializer;
    }

    abstract <R> R accept(Visitor<R> visitor);
}