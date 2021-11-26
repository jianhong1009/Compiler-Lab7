import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Stack;

public class Visitor extends lab7BaseVisitor<Void> {
    public PrintStream ps = new PrintStream(new FileOutputStream(Test.outputPath));
    public static String exp = "";
    public static ArrayList<Variable> variableList = new ArrayList<>();
    public static int num = 0;
    public static int ifNum = 0;
    public static int whileNum = 0;
    public static boolean endFlag = false;
    public static boolean funcFlag = false;
    public static boolean globalVarFlag = false;
    public static Stack<Integer> blockNumStack = new Stack<>();
    public static Stack<Integer> ifNumStack = new Stack<>();
    public static Stack<Integer> whileNumStack = new Stack<>();
    public static int blockNum = -1;
    public static String globalStorage = "";

    public Visitor() throws FileNotFoundException {
        System.setOut(ps);
    }

    @Override
    public Void visitCompUnit(lab7Parser.CompUnitContext ctx) {
        blockNumStack.push(++blockNum);
        for (lab7Parser.DeclContext e : ctx.decl()) {
            globalVarFlag = true;
            visit(e);
            globalVarFlag = false;
        }
        visit(ctx.funcDef(0));
        return null;
    }

    @Override
    public Void visitFuncDef(lab7Parser.FuncDefContext ctx) {
        System.out.println("declare i32 @getint()");
        System.out.println("declare void @putint(i32)");
        System.out.println("declare i32 @getch()");
        System.out.println("declare void @putch(i32)");
        System.out.println("define dso_local i32 @main() {");
        System.out.print(globalStorage);
        visit(ctx.block());
        System.out.println("}");
        blockNumStack.pop();
        return null;
    }

    @Override
    public Void visitFuncType(lab7Parser.FuncTypeContext ctx) {
        return super.visitFuncType(ctx);
    }

    @Override
    public Void visitBlock(lab7Parser.BlockContext ctx) {
        blockNumStack.push(++blockNum);
        for (lab7Parser.BlockItemContext e : ctx.blockItem()) {
            visit(e);
        }
        blockNumStack.pop();
        return null;
    }

    @Override
    public Void visitBlockItem(lab7Parser.BlockItemContext ctx) {
        return super.visitBlockItem(ctx);
    }

    @Override
    public Void visitStmt(lab7Parser.StmtContext ctx) {
        if (ctx.lVal() != null) {
            String var = ctx.lVal().getText();
            String str = "";

            if (var.contains("[") && var.contains("]") && Variable.isArray(ctx.lVal().ident().getText())) {
                if (ctx.lVal().exp().size() == 1) {
                    exp = "";
                    visit(ctx.lVal().exp(0));
                    str = Variable.getArrayElementStore(ctx.lVal().ident().getText(), exp, "");
                } else {
                    exp = "";
                    visit(ctx.lVal().exp(0));
                    String exp1 = exp;
                    exp = "";
                    visit(ctx.lVal().exp(1));
                    str = Variable.getArrayElementStore(ctx.lVal().ident().getText(), exp1, exp);
                }
            } else if (Variable.isConst(var) || Variable.isArray(var)) {
                System.exit(1);
            } else {
                str = Variable.getStore(var);
            }

            exp = "";
            visit(ctx.exp());
            String s = "";
            if (!funcFlag) {
                s = new PostfixExpression().func(exp);
            } else {
                s = "%" + num;
                funcFlag = false;
            }
            System.out.println("    store i32 " + s + ", i32* " + str);

        } else if (ctx.return_() != null) {
            exp = "";
            visit(ctx.exp());
            String s = "";
            if (!funcFlag) {
                s = new PostfixExpression().func(exp);
            } else {
                s = "%" + num;
                funcFlag = false;
            }
            System.out.println("    ret i32 " + s);
            endFlag = true;
        } else if (ctx.block() != null) {
            visit(ctx.block());
        } else if (ctx.if_() != null) {
            ifNum++;
            exp = "";
            visit(ctx.cond());
            String s = new PostfixExpression2().func(exp);

            System.out.println("    br i1 " + s + ",label %true" + ifNum + ", label %false" + ifNum);

            System.out.println("true" + ifNum + ":");
            int tempIfNum = ifNum;
            visit(ctx.stmt(0));

            if (endFlag) {
                endFlag = false;
            } else {
                System.out.println("    br label %end" + tempIfNum);
            }

            System.out.println("false" + tempIfNum + ":");
            if (ctx.stmt(1) != null) {
                visit(ctx.stmt(1));
            }
            if (endFlag) {
                endFlag = false;
            } else {
                System.out.println("    br label %end" + tempIfNum);
            }

            System.out.println("end" + tempIfNum + ":");
        } else if (ctx.while_() != null) {
            whileNum++;
            whileNumStack.push(whileNum);
            System.out.println("    br label %start_" + whileNum);

            exp = "";
            visit(ctx.cond());
            System.out.println("start_" + whileNum + ":");
            String s = new PostfixExpression2().func(exp);

            System.out.println("    br i1 " + s + ",label %true_" + whileNum + ", label %false_" + whileNum);

            System.out.println("true_" + whileNum + ":");
            int tempWhileNum = whileNum;
            visit(ctx.stmt(0));
            if (endFlag) {
                endFlag = false;
            } else {
                System.out.println("    br label %start_" + tempWhileNum);
            }

            System.out.println("false_" + tempWhileNum + ":");
            whileNumStack.pop();
        } else if (ctx.break_() != null) {
            if (!endFlag) {
                System.out.println("    br label %false_" + whileNumStack.peek());
                endFlag = true;
            }
        } else if (ctx.continue_() != null) {
            if (!endFlag) {
                System.out.println("    br label %start_" + whileNumStack.peek());
                endFlag = true;
            }
        } else {
            exp = "";
            if (ctx.exp() != null) {
                visit(ctx.exp());
                String s = "";
                if (!funcFlag) {
                    s = new PostfixExpression().func(exp);
                } else {
                    s = "%" + num;
                    funcFlag = false;
                }
            }
        }
        return null;
    }

    @Override
    public Void visitIf_(lab7Parser.If_Context ctx) {
        return super.visitIf_(ctx);
    }

    @Override
    public Void visitWhile_(lab7Parser.While_Context ctx) {
        return super.visitWhile_(ctx);
    }

    @Override
    public Void visitBreak_(lab7Parser.Break_Context ctx) {
        return super.visitBreak_(ctx);
    }

    @Override
    public Void visitContinue_(lab7Parser.Continue_Context ctx) {
        return super.visitContinue_(ctx);
    }

    @Override
    public Void visitReturn_(lab7Parser.Return_Context ctx) {
        return super.visitReturn_(ctx);
    }

    @Override
    public Void visitLVal(lab7Parser.LValContext ctx) {
        return super.visitLVal(ctx);
    }

    @Override
    public Void visitDecl(lab7Parser.DeclContext ctx) {
        return super.visitDecl(ctx);
    }

    @Override
    public Void visitConstDecl(lab7Parser.ConstDeclContext ctx) {
        return super.visitConstDecl(ctx);
    }

    @Override
    public Void visitBType(lab7Parser.BTypeContext ctx) {
        return super.visitBType(ctx);
    }

    @Override
    public Void visitConstDef(lab7Parser.ConstDefContext ctx) {
        Variable.checkRepeat(ctx.ident().getText());
        String var = ctx.ident().getText();
        int temp = 0;
        int cntArrDimension = 0;
        int[] arrLenArr = new int[100];
        String s = "";

        if (!globalVarFlag && ctx.constExp().size() == 0) {
            System.out.println("    %" + (num + 1) + " = alloca i32");
            temp = ++num;
        } else if (ctx.constExp().size() > 0) {
            int arrLen = 0;
            for (lab7Parser.ConstExpContext e : ctx.constExp()) {
                boolean tempGlobalFlag = globalVarFlag;
                globalVarFlag = true;
                exp = "";
                visit(e);
                s = new PostfixExpression().func(exp);
                if (Integer.parseInt(s) < 0) {
                    System.exit(1);
                }
                arrLenArr[arrLen++] = Integer.parseInt(s);
                globalVarFlag = tempGlobalFlag;
                cntArrDimension++;
            }

            if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = alloca ");
            else globalStorage += "    %" + (num + 1) + " = alloca ";

            for (int i = 0; i < cntArrDimension; i++) {
                if (!globalVarFlag) System.out.printf("[%d x ", arrLenArr[i]);
                else globalStorage += "[" + arrLenArr[i] + " x ";
            }
            if (!globalVarFlag) System.out.print("i32");
            else globalStorage += "i32";
            for (int i = 0; i < cntArrDimension; i++) {
                if (!globalVarFlag) System.out.print("]");
                else globalStorage += "]";
            }
            if (!globalVarFlag) System.out.println("");
            else globalStorage += "\n";
            temp = ++num;

            variableList.add(new Variable(var, "%" + temp, "null", 0, blockNumStack.peek(),
                    3, true, cntArrDimension, arrLenArr[0], arrLenArr[1]));
        }

        exp = "";
        visit(ctx.constInitVal());

        s = "";
        if (!funcFlag && ctx.constExp().size() == 0) {
            char[] str1 = exp.toCharArray();
            for (int i = 0; i < str1.length; i++) {
                String string = "";
                boolean flag = false;
                if (Character.isLetter(str1[i]) || str1[i] == '_') {
                    string += str1[i];
                    i++;
                    flag = true;
                    for (; i < str1.length && (Character.isDigit(str1[i]) || Character.isLetter(str1[i]) || str1[i] == '_'); i++) {
                        string += str1[i];
                    }
                }
                if (flag) {
                    Variable.checkExist(string);
                    if (!Variable.isConst(string)) {
                        System.exit(1);
                    }
                    i--;
                }
            }
            s = new PostfixExpression().func(exp);
        } else if (ctx.constExp().size() == 0) {
            s = "%" + num;
            funcFlag = false;
        }

        if (!globalVarFlag && ctx.constExp().size() == 0) {
            globalVarFlag = true;
            System.out.println("    store i32 " + s + ", i32* %" + temp);
            s = new PostfixExpression().func(exp);
            variableList.add(new Variable(var, "%" + temp, "null", Integer.parseInt(s), blockNumStack.peek(),
                    2, true, 0, 0, 0));
            globalVarFlag = false;

        } else if (ctx.constExp().size() == 0) {
            System.out.println("@" + var + " = dso_local global i32 " + s);
            variableList.add(new Variable(var, "@" + var, "null", Integer.parseInt(s), blockNumStack.peek(),
                    2, true, 0, 0, 0));

        } else if (ctx.constExp().size() > 0) {
            if ((exp.equals("{}") || exp.equals("{ }")) && cntArrDimension == 2) {
                exp = "{{}}";
            }
            char[] exp_ = exp.toCharArray();
            int temp_ = 0;
            boolean temp2 = false;
            for (char c : exp_) {
                if (c == '{') {
                    temp_++;
                } else if (c == '}') {
                    break;
                }
            }
            for (int i = 0; i < exp_.length; i++) {
                if (exp_[i] == '}') {
                    temp2 = true;
                } else if (exp_[i] == ',' && temp2) {
                    exp_[i] = '@';
                    temp2 = false;
                }
            }

            if (temp_ != cntArrDimension) {
                System.exit(1);
            }
            exp = new String(exp_);

            String[] split = null;
            if (cntArrDimension == 2) {
                exp = exp.strip();
                exp = exp.substring(1, exp.length() - 1);
                split = exp.split("@");
                if (split.length > arrLenArr[0]) {
                    System.exit(1);
                }
            }
            if (cntArrDimension == 1) {
                split = new String[1];
                split[0] = exp.strip();
            }
            int split1Num = 0;
            int split2Num = 0;

            int t1 = cntArrDimension == 1 ? 1 : arrLenArr[0];
            int t2 = cntArrDimension == 1 ? arrLenArr[0] : arrLenArr[1];
            for (int i = 0; i < t1; i++) {
                String s1 = "";
                assert split != null;
                if (i >= split.length) {
                    s1 = "{";
                    for (int i1 = 0; i1 < t2 - 1; i1++) {
                        s1 += "0,";
                    }
                    s1 += "0}";
                } else {
                    s1 = split[i];
                }

                s1 = s1.strip();
                s1 = s1.substring(1, s1.length() - 1);
                String[] split1 = s1.split(",");
                if (split1.length > arrLenArr[cntArrDimension - 1]) {
                    System.exit(1);
                }

                for (int i1 = 0; i1 < t2; i1++) {
                    String s2 = "";
                    if (i1 >= split1.length || split1[i1].equals("")) {
                        s2 = "0";
                    } else {
                        s2 = split1[i1];
                    }

                    if (cntArrDimension == 2) {
                        if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = getelementptr ");
                        else globalStorage += "    %" + (num + 1) + " = getelementptr ";
                        for (int i2 = 0; i2 < cntArrDimension; i2++) {
                            if (!globalVarFlag) System.out.printf("[%d x ", arrLenArr[i2]);
                            else globalStorage += "[" + arrLenArr[i2] + " x ";
                        }
                        if (!globalVarFlag) System.out.print("i32");
                        else globalStorage += "i32";
                        for (int i2 = 0; i2 < cntArrDimension; i2++) {
                            if (!globalVarFlag) System.out.print("]");
                            else globalStorage += "]";
                        }
                        if (!globalVarFlag) System.out.print(", ");
                        else globalStorage += ", ";
                        for (int i2 = 0; i2 < cntArrDimension; i2++) {
                            if (!globalVarFlag) System.out.printf("[%d x ", arrLenArr[i2]);
                            else globalStorage += "[" + arrLenArr[i2] + " x ";
                        }
                        if (!globalVarFlag) System.out.print("i32");
                        else globalStorage += "i32";
                        for (int i2 = 0; i2 < cntArrDimension; i2++) {
                            if (!globalVarFlag) System.out.print("]");
                            else globalStorage += "]";
                        }
                        if (!globalVarFlag)
                            System.out.println("* " + Variable.getArrayStore(var) + ", i32 0, i32 " + split1Num);
                        else globalStorage += "* " + Variable.getArrayStore(var) + ", i32 0, i32 " + split1Num + "\n";
                        num++;

                        if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = getelementptr ");
                        else globalStorage += "    %" + (num + 1) + " = getelementptr ";

                        if (!globalVarFlag) System.out.printf("[%d x i32], [%d x i32]* ", arrLenArr[1], arrLenArr[1]);
                        else globalStorage += "[" + arrLenArr[1] + " x i32], [" + arrLenArr[1] + " x i32]* ";

                        if (!globalVarFlag) System.out.println("%" + num + ", i32 0, i32 " + split2Num);
                        else globalStorage += "%" + num + ", i32 0, i32 " + split2Num + "\n";
                        temp = ++num;
                    } else {
                        if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = getelementptr ");
                        else globalStorage += "    %" + (num + 1) + " = getelementptr ";

                        if (!globalVarFlag) System.out.printf("[%d x i32], [%d x i32]* ", arrLenArr[0], arrLenArr[0]);
                        else globalStorage += "[" + arrLenArr[0] + " x i32], [" + arrLenArr[0] + " x i32]* ";

                        if (!globalVarFlag)
                            System.out.println(Variable.getArrayStore(var) + ", i32 0, i32 " + split2Num);
                        else globalStorage += Variable.getArrayStore(var) + ", i32 0, i32 " + split2Num + "\n";
                        temp = ++num;
                    }

                    s = new PostfixExpression().func(s2);
                    if (!globalVarFlag) System.out.println("    store i32 " + s + ", i32* %" + temp);
                    else globalStorage += "    store i32 " + s + ", i32* %" + temp + "\n";

                    if (cntArrDimension == 1) {
                        variableList.add(new Variable(var + "[" + split2Num + "]", "%" + temp, "null", 0, blockNumStack.peek(),
                                2, true, 0, 0, 0));
                    } else {
                        variableList.add(new Variable(var + "[" + split1Num + "][" + split2Num + "]", "%" + temp, "null", 0, blockNumStack.peek(),
                                2, true, 0, 0, 0));
                    }

                    split2Num++;
                }

                split1Num++;
                split2Num = 0;
            }
        }

        return null;
    }

    @Override
    public Void visitConstInitVal(lab7Parser.ConstInitValContext ctx) {
        return super.visitConstInitVal(ctx);
    }

    @Override
    public Void visitConstExp(lab7Parser.ConstExpContext ctx) {
        return super.visitConstExp(ctx);
    }

    @Override
    public Void visitVarDecl(lab7Parser.VarDeclContext ctx) {
        return super.visitVarDecl(ctx);
    }

    @Override
    public Void visitVarDef(lab7Parser.VarDefContext ctx) {
        Variable.checkRepeat(ctx.ident().getText());
        if (ctx.initVal() == null) {
            String var = ctx.ident().getText();
            int temp = 0;
            int cntArrDimension = 0;
            int[] arrLenArr = new int[100];
            String s = "";

            if (!globalVarFlag && ctx.constExp().size() == 0) {
                System.out.println("    %" + (num + 1) + " = alloca i32");
                temp = ++num;
                variableList.add(new Variable(var, "%" + temp, "null", 0, blockNumStack.peek(),
                        1, false, 0, 0, 0));

            } else if (globalVarFlag && ctx.constExp().size() == 0) {
                System.out.println("@" + var + " = dso_local global i32 0");
                variableList.add(new Variable(var, "@" + var, "null", 0, blockNumStack.peek(),
                        1, false, 0, 0, 0));

            } else {
                int arrLen = 0;
                for (lab7Parser.ConstExpContext e : ctx.constExp()) {
                    boolean tempGlobalFlag = globalVarFlag;
                    globalVarFlag = true;
                    exp = "";
                    visit(e);
                    s = new PostfixExpression().func(exp);
                    if (Integer.parseInt(s) < 0) {
                        System.exit(1);
                    }
                    arrLenArr[arrLen++] = Integer.parseInt(s);
                    globalVarFlag = tempGlobalFlag;
                    cntArrDimension++;
                }

                if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = alloca ");
                else globalStorage += "    %" + (num + 1) + " = alloca ";

                for (int i = 0; i < cntArrDimension; i++) {
                    if (!globalVarFlag) System.out.printf("[%d x ", arrLenArr[i]);
                    else globalStorage += "[" + arrLenArr[i] + " x ";
                }
                if (!globalVarFlag) System.out.print("i32");
                else globalStorage += "i32";
                for (int i = 0; i < cntArrDimension; i++) {
                    if (!globalVarFlag) System.out.print("]");
                    else globalStorage += "]";
                }
                if (!globalVarFlag) System.out.println("");
                else globalStorage += "\n";
                temp = ++num;

                variableList.add(new Variable(var, "%" + temp, "null", 0, blockNumStack.peek(),
                        3, false, cntArrDimension, arrLenArr[0], arrLenArr[1]));

                int split1Num = 0;
                int split2Num = 0;

                int t1 = cntArrDimension == 1 ? 1 : arrLenArr[0];
                int t2 = cntArrDimension == 1 ? arrLenArr[0] : arrLenArr[1];
                for (int i = 0; i < t1; i++) {
                    String s1 = "";
                    s1 = "{";
                    for (int i1 = 0; i1 < t2 - 1; i1++) {
                        s1 += "0,";
                    }
                    s1 += "0}";

                    s1 = s1.strip();
                    s1 = s1.substring(1, s1.length() - 1);
                    String[] split1 = s1.split(",");
                    if (split1.length > arrLenArr[cntArrDimension - 1]) {
                        System.exit(1);
                    }

                    for (int i1 = 0; i1 < t2; i1++) {
                        String s2 = "";
                        if (i1 >= split1.length || split1[i1].equals("")) {
                            s2 = "0";
                        } else {
                            s2 = split1[i1];
                        }

                        if (cntArrDimension == 2) {
                            if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = getelementptr ");
                            else globalStorage += "    %" + (num + 1) + " = getelementptr ";
                            for (int i2 = 0; i2 < cntArrDimension; i2++) {
                                if (!globalVarFlag) System.out.printf("[%d x ", arrLenArr[i2]);
                                else globalStorage += "[" + arrLenArr[i2] + " x ";
                            }
                            if (!globalVarFlag) System.out.print("i32");
                            else globalStorage += "i32";
                            for (int i2 = 0; i2 < cntArrDimension; i2++) {
                                if (!globalVarFlag) System.out.print("]");
                                else globalStorage += "]";
                            }
                            if (!globalVarFlag) System.out.print(", ");
                            else globalStorage += ", ";
                            for (int i2 = 0; i2 < cntArrDimension; i2++) {
                                if (!globalVarFlag) System.out.printf("[%d x ", arrLenArr[i2]);
                                else globalStorage += "[" + arrLenArr[i2] + " x ";
                            }
                            if (!globalVarFlag) System.out.print("i32");
                            else globalStorage += "i32";
                            for (int i2 = 0; i2 < cntArrDimension; i2++) {
                                if (!globalVarFlag) System.out.print("]");
                                else globalStorage += "]";
                            }
                            if (!globalVarFlag)
                                System.out.println("* " + Variable.getArrayStore(var) + ", i32 0, i32 " + split1Num);
                            else
                                globalStorage += "* " + Variable.getArrayStore(var) + ", i32 0, i32 " + split1Num + "\n";
                            num++;

                            if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = getelementptr ");
                            else globalStorage += "    %" + (num + 1) + " = getelementptr ";

                            if (!globalVarFlag)
                                System.out.printf("[%d x i32], [%d x i32]* ", arrLenArr[1], arrLenArr[1]);
                            else globalStorage += "[" + arrLenArr[1] + " x i32], [" + arrLenArr[1] + " x i32]* ";

                            if (!globalVarFlag) System.out.println("%" + num + ", i32 0, i32 " + split2Num);
                            else globalStorage += "%" + num + ", i32 0, i32 " + split2Num + "\n";
                            temp = ++num;
                        } else {
                            if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = getelementptr ");
                            else globalStorage += "    %" + (num + 1) + " = getelementptr ";

                            if (!globalVarFlag)
                                System.out.printf("[%d x i32], [%d x i32]* ", arrLenArr[0], arrLenArr[0]);
                            else globalStorage += "[" + arrLenArr[0] + " x i32], [" + arrLenArr[0] + " x i32]* ";

                            if (!globalVarFlag)
                                System.out.println(Variable.getArrayStore(var) + ", i32 0, i32 " + split2Num);
                            else globalStorage += Variable.getArrayStore(var) + ", i32 0, i32 " + split2Num + "\n";
                            temp = ++num;
                        }

                        s = new PostfixExpression().func(s2);
                        if (!globalVarFlag) System.out.println("    store i32 " + s + ", i32* %" + temp);
                        else globalStorage += "    store i32 " + s + ", i32* %" + temp + "\n";

                        if (cntArrDimension == 1) {
                            variableList.add(new Variable(var + "[" + split2Num + "]", "%" + temp, "null", 0, blockNumStack.peek(),
                                    2, false, 0, 0, 0));
                        } else {
                            variableList.add(new Variable(var + "[" + split1Num + "][" + split2Num + "]", "%" + temp, "null", 0, blockNumStack.peek(),
                                    2, false, 0, 0, 0));
                        }

                        split2Num++;
                    }

                    split1Num++;
                    split2Num = 0;
                }
            }

        } else {
            String var = ctx.ident().getText();
            int temp = 0;
            int cntArrDimension = 0;
            int[] arrLenArr = new int[100];
            String s = "";

            if (!globalVarFlag && ctx.constExp().size() == 0) {
                System.out.println("    %" + (num + 1) + " = alloca i32");
                temp = ++num;
            } else if (ctx.constExp().size() > 0) {
                int arrLen = 0;
                for (lab7Parser.ConstExpContext e : ctx.constExp()) {
                    boolean tempGlobalFlag = globalVarFlag;
                    globalVarFlag = true;
                    exp = "";
                    visit(e);
                    s = new PostfixExpression().func(exp);
                    if (Integer.parseInt(s) < 0) {
                        System.exit(1);
                    }
                    arrLenArr[arrLen++] = Integer.parseInt(s);
                    globalVarFlag = tempGlobalFlag;
                    cntArrDimension++;
                }

                if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = alloca ");
                else globalStorage += "    %" + (num + 1) + " = alloca ";

                for (int i = 0; i < cntArrDimension; i++) {
                    if (!globalVarFlag) System.out.printf("[%d x ", arrLenArr[i]);
                    else globalStorage += "[" + arrLenArr[i] + " x ";
                }
                if (!globalVarFlag) System.out.print("i32");
                else globalStorage += "i32";
                for (int i = 0; i < cntArrDimension; i++) {
                    if (!globalVarFlag) System.out.print("]");
                    else globalStorage += "]";
                }
                if (!globalVarFlag) System.out.println("");
                else globalStorage += "\n";
                temp = ++num;

                variableList.add(new Variable(var, "%" + temp, "null", 0, blockNumStack.peek(),
                        3, false, cntArrDimension, arrLenArr[0], arrLenArr[1]));
            }

            exp = "";
            visit(ctx.initVal());

            if (!funcFlag && ctx.constExp().size() == 0) {
                s = new PostfixExpression().func(exp);
            } else if (ctx.constExp().size() == 0) {
                s = "%" + num;
                funcFlag = false;
            }

            if (!globalVarFlag && ctx.constExp().size() == 0) {
                System.out.println("    store i32 " + s + ", i32* %" + temp);
                variableList.add(new Variable(var, "%" + temp, "null", 0, blockNumStack.peek(),
                        2, false, 0, 0, 0));

            } else if (ctx.constExp().size() == 0) {
                System.out.println("@" + var + " = dso_local global i32 " + s);
                variableList.add(new Variable(var, "@" + var, "null", 0, blockNumStack.peek(),
                        2, false, 0, 0, 0));

            } else if (ctx.constExp().size() > 0) {
                if ((exp.equals("{}") || exp.equals("{ }")) && cntArrDimension == 2) {
                    exp = "{{}}";
                }
                char[] exp_ = exp.toCharArray();
                int temp_ = 0;
                boolean temp2 = false;
                for (char c : exp_) {
                    if (c == '{') {
                        temp_++;
                    } else if (c == '}') {
                        break;
                    }
                }
                for (int i = 0; i < exp_.length; i++) {
                    if (exp_[i] == '}') {
                        temp2 = true;
                    } else if (exp_[i] == ',' && temp2) {
                        exp_[i] = '@';
                        temp2 = false;
                    }
                }

                if (temp_ != cntArrDimension) {
                    System.exit(1);
                }
                exp = new String(exp_);

                String[] split = null;
                if (cntArrDimension == 2) {
                    exp = exp.strip();
                    exp = exp.substring(1, exp.length() - 1);
                    split = exp.split("@");
                    if (split.length > arrLenArr[0]) {
                        System.exit(1);
                    }
                }
                if (cntArrDimension == 1) {
                    split = new String[1];
                    split[0] = exp.strip();
                }
                int split1Num = 0;
                int split2Num = 0;

                int t1 = cntArrDimension == 1 ? 1 : arrLenArr[0];
                int t2 = cntArrDimension == 1 ? arrLenArr[0] : arrLenArr[1];
                for (int i = 0; i < t1; i++) {
                    String s1 = "";
                    assert split != null;
                    if (i >= split.length) {
                        s1 = "{";
                        for (int i1 = 0; i1 < t2 - 1; i1++) {
                            s1 += "0,";
                        }
                        s1 += "0}";
                    } else {
                        s1 = split[i];
                    }

                    s1 = s1.strip();
                    s1 = s1.substring(1, s1.length() - 1);
                    String[] split1 = s1.split(",");
                    if (split1.length > arrLenArr[cntArrDimension - 1]) {
                        System.exit(1);
                    }

                    for (int i1 = 0; i1 < t2; i1++) {
                        String s2 = "";
                        if (i1 >= split1.length || split1[i1].equals("")) {
                            s2 = "0";
                        } else {
                            s2 = split1[i1];
                        }

                        if (cntArrDimension == 2) {
                            if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = getelementptr ");
                            else globalStorage += "    %" + (num + 1) + " = getelementptr ";
                            for (int i2 = 0; i2 < cntArrDimension; i2++) {
                                if (!globalVarFlag) System.out.printf("[%d x ", arrLenArr[i2]);
                                else globalStorage += "[" + arrLenArr[i2] + " x ";
                            }
                            if (!globalVarFlag) System.out.print("i32");
                            else globalStorage += "i32";
                            for (int i2 = 0; i2 < cntArrDimension; i2++) {
                                if (!globalVarFlag) System.out.print("]");
                                else globalStorage += "]";
                            }
                            if (!globalVarFlag) System.out.print(", ");
                            else globalStorage += ", ";
                            for (int i2 = 0; i2 < cntArrDimension; i2++) {
                                if (!globalVarFlag) System.out.printf("[%d x ", arrLenArr[i2]);
                                else globalStorage += "[" + arrLenArr[i2] + " x ";
                            }
                            if (!globalVarFlag) System.out.print("i32");
                            else globalStorage += "i32";
                            for (int i2 = 0; i2 < cntArrDimension; i2++) {
                                if (!globalVarFlag) System.out.print("]");
                                else globalStorage += "]";
                            }
                            if (!globalVarFlag)
                                System.out.println("* " + Variable.getArrayStore(var) + ", i32 0, i32 " + split1Num);
                            else
                                globalStorage += "* " + Variable.getArrayStore(var) + ", i32 0, i32 " + split1Num + "\n";
                            num++;

                            if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = getelementptr ");
                            else globalStorage += "    %" + (num + 1) + " = getelementptr ";

                            if (!globalVarFlag)
                                System.out.printf("[%d x i32], [%d x i32]* ", arrLenArr[1], arrLenArr[1]);
                            else globalStorage += "[" + arrLenArr[1] + " x i32], [" + arrLenArr[1] + " x i32]* ";

                            if (!globalVarFlag) System.out.println("%" + num + ", i32 0, i32 " + split2Num);
                            else globalStorage += "%" + num + ", i32 0, i32 " + split2Num + "\n";
                            temp = ++num;
                        } else {
                            if (!globalVarFlag) System.out.print("    %" + (num + 1) + " = getelementptr ");
                            else globalStorage += "    %" + (num + 1) + " = getelementptr ";

                            if (!globalVarFlag)
                                System.out.printf("[%d x i32], [%d x i32]* ", arrLenArr[0], arrLenArr[0]);
                            else globalStorage += "[" + arrLenArr[0] + " x i32], [" + arrLenArr[0] + " x i32]* ";

                            if (!globalVarFlag)
                                System.out.println(Variable.getArrayStore(var) + ", i32 0, i32 " + split2Num);
                            else globalStorage += Variable.getArrayStore(var) + ", i32 0, i32 " + split2Num + "\n";
                            temp = ++num;
                        }

                        s = new PostfixExpression().func(s2);
                        if (!globalVarFlag) System.out.println("    store i32 " + s + ", i32* %" + temp);
                        else globalStorage += "    store i32 " + s + ", i32* %" + temp + "\n";

                        if (cntArrDimension == 1) {
                            variableList.add(new Variable(var + "[" + split2Num + "]", "%" + temp, "null", 0, blockNumStack.peek(),
                                    2, false, 0, 0, 0));
                        } else {
                            variableList.add(new Variable(var + "[" + split1Num + "][" + split2Num + "]", "%" + temp, "null", 0, blockNumStack.peek(),
                                    2, false, 0, 0, 0));
                        }

                        split2Num++;
                    }

                    split1Num++;
                    split2Num = 0;
                }
            }
        }
        return null;
    }

    @Override
    public Void visitInitVal(lab7Parser.InitValContext ctx) {
        return super.visitInitVal(ctx);
    }

    @Override
    public Void visitCond(lab7Parser.CondContext ctx) {
        return super.visitCond(ctx);
    }

    @Override
    public Void visitExp(lab7Parser.ExpContext ctx) {
        return super.visitExp(ctx);
    }

    @Override
    public Void visitLOrExp(lab7Parser.LOrExpContext ctx) {
        if (ctx.lOrExp() != null) {
            visit(ctx.lOrExp());
            exp += "|";
            visit(ctx.lAndExp());
        } else {
            visit(ctx.lAndExp());
        }
        return null;
    }

    @Override
    public Void visitLAndExp(lab7Parser.LAndExpContext ctx) {
        if (ctx.lAndExp() != null) {
            visit(ctx.lAndExp());
            exp += "&";
            visit(ctx.eqExp());
        } else {
            visit(ctx.eqExp());
        }
        return null;
    }

    @Override
    public Void visitEqExp(lab7Parser.EqExpContext ctx) {
        return super.visitEqExp(ctx);
    }

    @Override
    public Void visitEqNeq(lab7Parser.EqNeqContext ctx) {
        if (ctx.getText().equals("==")) {
            exp += "="; // ==
        } else {
            exp += "~"; // !=
        }
        return null;
    }

    @Override
    public Void visitRelExp(lab7Parser.RelExpContext ctx) {
        return super.visitRelExp(ctx);
    }

    @Override
    public Void visitCompare(lab7Parser.CompareContext ctx) {
        if (ctx.getText().equals("<=")) {
            exp += "《"; // <=
        } else if (ctx.getText().equals(">=")) {
            exp += "》"; // >=
        } else {
            exp += ctx.getText(); // < >
        }
        return null;
    }

    @Override
    public Void visitAddExp(lab7Parser.AddExpContext ctx) {
        return super.visitAddExp(ctx);
    }

    @Override
    public Void visitAddSub(lab7Parser.AddSubContext ctx) {
        exp += ctx.getText();
        return null;
    }

    @Override
    public Void visitMulExp(lab7Parser.MulExpContext ctx) {
        return super.visitMulExp(ctx);
    }

    @Override
    public Void visitMulDiv(lab7Parser.MulDivContext ctx) {
        exp += ctx.getText();
        return null;
    }

    @Override
    public Void visitUnaryExp(lab7Parser.UnaryExpContext ctx) {
        if (ctx.primaryExp() != null) {
            visit(ctx.primaryExp());
        } else if (ctx.ident() != null) {
            String func = ctx.ident().getText();
            if (func.equals("getint") && ctx.funcRParams() == null) {
//                    System.out.println("    %" + (num + 1) + " = call i32 @getint()");
//                    num++;
//                    funcFlag = true;
                exp += "@getint";
            } else if (func.equals("putint") && ctx.funcRParams() != null) {
                exp = "";
                visit(ctx.funcRParams());
                String s = new PostfixExpression().func(exp);
                System.out.println("    call void @putint(i32 " + s + ")");
                funcFlag = true;
            } else if (func.equals("getch") && ctx.funcRParams() == null) {
//                    System.out.println("    %" + (num + 1) + " = call i32 @getch()");
//                    num++;
//                    funcFlag = true;
                exp += "@getch";
            } else if (func.equals("putch") && ctx.funcRParams() != null) {
                exp = "";
                visit(ctx.funcRParams());
                String s = new PostfixExpression().func(exp);
                System.out.println("    call void @putch(i32 " + s + ")");
                funcFlag = true;
            } else if (func.equals("getarray") && ctx.funcRParams() != null) {
                exp += "@getarray";
            } else if (func.equals("putarray") && ctx.funcRParams() != null) {
                exp = "";
                visit(ctx.funcRParams());
                String[] split = exp.split(",");
                String s1 = new PostfixExpression().func(split[0]);
                System.out.println("    call void @putarray(i32 " + s1 + ", " + Variable.getArrayFormat(split[1]) + ")");
                funcFlag = true;
            } else {
                System.exit(1);
            }

        } else {
            visit(ctx.unaryOp());
            visit(ctx.unaryExp());
        }
        return null;
    }

    @Override
    public Void visitFuncRParams(lab7Parser.FuncRParamsContext ctx) {
        return super.visitFuncRParams(ctx);
    }

    @Override
    public Void visitPrimaryExp(lab7Parser.PrimaryExpContext ctx) {
        if (ctx.exp() != null) {
            exp += "(";
            visit(ctx.exp());
            exp += ")";
        } else if (ctx.lVal() != null) {
            exp += ctx.lVal().getText();
        } else {
            visit(ctx.number());
        }
        return null;
    }

    @Override
    public Void visitUnaryOp(lab7Parser.UnaryOpContext ctx) {
        exp += ctx.getText();
        return null;
    }

    @Override
    public Void visitIdent(lab7Parser.IdentContext ctx) {
        return super.visitIdent(ctx);
    }

    @Override
    public Void visitNumber(lab7Parser.NumberContext ctx) {
        if (ctx.DecimalConst() != null) {
            exp += ctx.DecimalConst().getText();
        } else if (ctx.OctalConst() != null) {
            if (ctx.OctalConst().getText().equals("0")) {
                exp += "0";
            } else {
                String s = ctx.OctalConst().getText().substring(1);
                exp += String.valueOf(Integer.parseInt(s, 8));
            }
        } else {
            String s = ctx.HexadecimalConst().getText().substring(2);
            exp += String.valueOf(Integer.parseInt(s, 16));
        }
        return null;
    }

    @Override
    public Void visitComma(lab7Parser.CommaContext ctx) {
        exp += ",";
        return null;
    }

    @Override
    public Void visitOpenBracket(lab7Parser.OpenBracketContext ctx) {
        exp += "{";
        return null;
    }

    @Override
    public Void visitCloseBracket(lab7Parser.CloseBracketContext ctx) {
        exp += "}";
        return null;
    }

    public int testing() {

        return 1;
    }
}
