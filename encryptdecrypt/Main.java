package encryptdecrypt;

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class Main {
    static String mode = "enc";
    static int key = 0;
    static String data = "";
    static String inFileName = "";
    static String outFileName = "";
    static String algorithm = "shift";

    public static void main(String[] args) {

        List<String> argList = Arrays.asList(args);
        Map<String, String> argMap = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            argMap.put(argList.get(i), argList.get(i + 1));
        }
        if (!argMap.isEmpty()) {
            if (argMap.get("-mode") != null) {
                mode = argMap.get("-mode");
//                System.out.println(mode);
            }
            if (argMap.get("-key") != null) {
                key = Integer.parseInt(argMap.get("-key"));
//                System.out.println(key);
            }
            if (argMap.get("-data") != null) {
                data = argMap.get("-data");
//                System.out.println(data);
            }
            if (argMap.get("-in") != null) {
                inFileName = argMap.get("-in");
//                System.out.println(inFileName);
            }
            if (argMap.get("-out") != null) {
                outFileName = argMap.get("-out");
//                System.out.println(outFileName);
            }
            if (argMap.get("-alg") != null) {
                algorithm = argMap.get("-alg");
//                System.out.println(outFileName);
            }
        }
/*
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        String text = scanner.nextLine();
        int key = Integer.parseInt(scanner.nextLine().trim());
        scanner.close();
*/
        if ("".equals(data) && !"".equals(inFileName)) {
            if (!ReadText.isExist(inFileName)) {
                System.out.println(String.format("Error file %s not exist", inFileName));
                return;
            }
            data = ReadText.readAllWithoutEol(inFileName);
        }

        String output = "";
        if ("shift".equals(algorithm)) {
            if ("enc".equals(mode)) {
                output = Shift.encript(data, key);
            } else {
                output = Shift.decript(data, key);
            }
        } else {
            if ("enc".equals(mode)) {
                output = Unicode.encript(data, key);
            } else {
                output = Unicode.decript(data, key);
            }
        }

        if (!"".equals(outFileName)) {
            WriteText.writeAll(outFileName, output);
        } else {
            System.out.println(output);
        }
    }
}

class Unicode {

    static String encript(String data, int key) {
        return data.chars().map(ch -> ch + key).mapToObj(i -> (char)i).map(c->c.toString()).collect(Collectors.joining());
    }

    static String decript(String data, int key) {
        return data.chars().map(ch -> ch - key).mapToObj(i -> (char)i).map(c->c.toString()).collect(Collectors.joining());      
    }
}

class Shift {

    static String encript(String data, int key) {
        return data.chars().map(ch ->encriptChar(ch, key)).mapToObj(i -> (char)i).map(c->c.toString()).collect(Collectors.joining());
    }

    static String decript(String data, int key) {
        return data.chars().map(ch ->decriptChar(ch, key)).mapToObj(i -> (char)i).map(c->c.toString()).collect(Collectors.joining());
    }

    static int encriptChar(int code, int key) {
        int low = (int) 'a';
        int high = (int) 'z';
        if (code < low || code > high) {
            return code;
        }
        int newCode = code + key;
        if (newCode > high) {
            newCode = newCode - (high - low + 1);
        }
        return newCode;
    }

    static int decriptChar(int code, int key) {
        int low = (int) 'a';
        int high = (int) 'z';
        if (code < low || code > high) {
            return code;
        }
        int newCode = code - key;
        if (newCode < low) {
            newCode = newCode + (high - low + 1);
        }
        return newCode;
    }
}

class ReadText {

    static boolean isExist(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    static String getAbsolutePath(String fileName) {
        File file = new File(fileName);
        return file.getAbsolutePath();
    }

    static String readAllWithoutEol(String fileName) {
        String text = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));   
            text =  br.lines().collect(Collectors.joining());        
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return text;
    }

    static List<String> readLines(String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));   
            lines =  br.lines().collect(Collectors.toList());        
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return lines;
    }

    static String readAll(String fileName) {
        char[] cbuf = new char[4096];
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));           
            while (true) {
                int length = br.read(cbuf, 0, cbuf.length);
                if (length != -1) {
                    sb.append(cbuf, 0, length);
                }
                if (length < cbuf.length) {
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }
}

class WriteText {

    static void writeAll(String fileName, String text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            bw.write(text, 0, text.length());
            bw.close();
        } catch (IOException e) {
            System.out.println("Error" + e.getMessage());
        }
    }
}
