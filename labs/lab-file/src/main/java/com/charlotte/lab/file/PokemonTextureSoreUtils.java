package com.charlotte.lab.file;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Charlotte
 */
public class PokemonTextureSoreUtils {

    @AllArgsConstructor
    enum Match {
        Front(Pattern.compile("^[0-9]{3}(_[0-9]){0,1}\\.gif$")),
        FrontShiny(Pattern.compile("^[0-9]{3}s(_[0-9]){0,1}\\.gif$")),
        Back(Pattern.compile("^[0-9]{3}b(_[0-9]){0,1}\\.gif$")),
        BackShiny(Pattern.compile("^[0-9]{3}sb(_[0-9]){0,1}\\.gif$")),
        ;

        private Pattern pattern;

    }


    public static void main(String[] args) throws IOException {
        String pre = "D:\\testure\\";
        File file = new File("E:\\Photo\\Battlers");
        for (File textureFile : file.listFiles()) {
            String name = textureFile.getName();
            if (!name.endsWith(".gif")) {
                continue;
            }
            boolean match = false;
            for (Match value : Match.values()) {
                if (!value.pattern.matcher(name).matches()) {
                    continue;
                }
                match = true;
                File type = new File(pre + value.name());
                if(!type.exists()){
                    type.mkdirs();
                }
                String newName = name.substring(0, 3) + ".gif";
                File newFile = new File(pre + value.name() + "\\" + newName);
                newFile.createNewFile();

                FileInputStream inputStream = new FileInputStream(textureFile);
                FileOutputStream outputStream = new FileOutputStream(newFile);
                byte[] bytes = new byte[1024];
                int i;
                while ((i = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, i);
                }
            }
            if (!match){
                System.out.println("未匹配文件" + name);
            }
        }
    }


}
