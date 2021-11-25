public class Variable {
    public String name;
    public String store;
    public String load;
    public int value;
    public int blockNum;
    public int status; // 1表示定义了，还未赋值，2表示定义了也赋值了
    public boolean isConst;

    public Variable(String name, String store, String load, int value, int blockNum, int status, boolean isConst) {
        this.name = name;
        this.store = store;
        this.load = load;
        this.value = value;
        this.blockNum = blockNum;
        this.status = status;
        this.isConst = isConst;
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
}
