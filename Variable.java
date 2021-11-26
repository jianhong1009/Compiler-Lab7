public class Variable {
    public String name;
    public String store;
    public String load;
    public int value;
    public int blockNum;
    public int status; // 1表示定义了，还未赋值，2表示定义了也赋值了
    public boolean isConst;
    public int arrDimension;
    public int arrSize1;
    public int arrSize2;

    public Variable(String name, String store, String load, int value, int blockNum, int status, boolean isConst, int arrDimension, int arrSize1, int arrSize2) {
        this.name = name;
        this.store = store;
        this.load = load;
        this.value = value;
        this.blockNum = blockNum;
        this.status = status;
        this.isConst = isConst;
        this.arrDimension = arrDimension;
        this.arrSize1 = arrSize1;
        this.arrSize2 = arrSize2;
    }

    public static String getStore(String name) {
        for (int i = Visitor.blockNumStack.size() - 1; i >= 0; i--) {
            for (Variable v : Visitor.variableList) {
                if (v.name.equals(name) && v.blockNum == Visitor.blockNumStack.get(i) && v.status != 3) {
                    return v.store;
                }
            }
        }
        System.exit(1);
        return null;
    }

    public static Variable getArray(String name) {
        for (int i = Visitor.blockNumStack.size() - 1; i >= 0; i--) {
            for (Variable v : Visitor.variableList) {
                if (v.name.equals(name) && v.blockNum == Visitor.blockNumStack.get(i) && v.status == 3) {
                    return v;
                }
            }
        }
        System.exit(1);
        return null;
    }

    public static String getArrayStore(String name) {
        for (int i = Visitor.blockNumStack.size() - 1; i >= 0; i--) {
            for (Variable v : Visitor.variableList) {
                if (v.name.equals(name) && v.blockNum == Visitor.blockNumStack.get(i) && v.status == 3) {
                    return v.store;
                }
            }
        }
        System.exit(1);
        return null;
    }

    public static String getLoad(String name) {
        for (Variable v : Visitor.variableList) {
            if (v.name.equals(name)) {
                return v.load;
            }
        }
        return null;
    }

    public static void setLoad(String name, String load) {
        for (Variable v : Visitor.variableList) {
            if (v.name.equals(name)) {
                v.load = load;
            }
        }
    }

    public static String getGlobalVarValue(String name) {
        for (int i = Visitor.blockNumStack.size() - 1; i >= 0; i--) {
            for (Variable v : Visitor.variableList) {
                if (v.name.equals(name) && v.blockNum == Visitor.blockNumStack.get(i)) {
                    return String.valueOf(v.value);
                }
            }
        }
        System.exit(1);
        return null;
    }

    public static void checkRepeat(String name) {
        for (Variable v : Visitor.variableList) {
            if (v.name.equals(name) && v.blockNum == Visitor.blockNumStack.peek()) {
                System.exit(1);
            }
        }
    }

    public static void checkExist(String name) {
        for (Variable v : Visitor.variableList) {
            if (v.name.equals(name)) {
                return;
            }
        }
        System.exit(1);
    }

    public static boolean isConst(String name) {
        for (int i = Visitor.blockNumStack.size() - 1; i >= 0; i--) {
            for (Variable v : Visitor.variableList) {
                if (v.name.equals(name) && v.isConst && v.blockNum == Visitor.blockNumStack.get(i)) {
                    return true;
                } else if (v.name.equals(name) && v.blockNum == Visitor.blockNumStack.get(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isArray(String name) {
        for (int i = Visitor.blockNumStack.size() - 1; i >= 0; i--) {
            for (Variable v : Visitor.variableList) {
                if (v.name.equals(name) && v.status == 3 && v.blockNum == Visitor.blockNumStack.get(i)) {
                    return true;
                } else if (v.name.equals(name) && v.blockNum == Visitor.blockNumStack.get(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getArrayFormat(String name) {
        for (int i = Visitor.blockNumStack.size() - 1; i >= 0; i--) {
            for (Variable v : Visitor.variableList) {
                if (v.name.equals(name) && v.blockNum == Visitor.blockNumStack.get(i) && v.status == 3) {
                    String s = "[" + v.value + " x i32]* " + v.store;
                    return s;
                }
            }
        }
        System.exit(1);
        return null;
    }

    public static String getArrayElementStore(String arrayName, String exp1, String exp2) {
        Variable v = getArray(arrayName);
        if (v.arrDimension == 1) {
            //%4 = getelementptr inbounds [1 x i32], [1 x i32]* %2, i64 0, i64 0
            String s1 = new PostfixExpression().func(exp1);
            System.out.print("    %" + (Visitor.num + 1) + " = getelementptr ");
            System.out.printf("[%d x i32], [%d x i32]* %s, i32 0, i32 %s\n", v.arrSize1, v.arrSize1, v.store, s1);
            Visitor.num++;
            return "%" + Visitor.num;
        } else if (v.arrDimension == 2) {
            String s1 = new PostfixExpression().func(exp1);
            String s2 = new PostfixExpression().func(exp2);
            System.out.print("    %" + (Visitor.num + 1) + " = getelementptr ");
            System.out.printf("[%d x [%d x i32]], [%d x [%d x i32]]* %s, i32 0, i32 %s\n", v.arrSize1, v.arrSize2, v.arrSize1, v.arrSize2, v.store, s1);
            Visitor.num++;
            System.out.print("    %" + (Visitor.num + 1) + " = getelementptr ");
            System.out.printf("[%d x i32], [%d x i32]* %s, i32 0, i32 %s\n", v.arrSize2, v.arrSize2, "%" + Visitor.num, s2);
            Visitor.num++;
            return "%" + Visitor.num;

        }
        return null;
    }
}
