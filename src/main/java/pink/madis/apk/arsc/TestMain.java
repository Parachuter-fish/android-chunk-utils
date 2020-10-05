package pink.madis.apk.arsc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @Author : wangjian
 * @Data : 2020-09-12 18:30
 * @Describe :
 */
public class TestMain {
    public static final boolean DEBUG = true;
    public static void main(String[] args) throws IOException {
//        System.out.println(new File("./").getAbsolutePath());
        final String PATH = "resources.arsc";
        File file = new File(PATH);
        ResourceFile resourceFile = null;
        try {
            resourceFile = ResourceFile.fromInputStream(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Chunk chunk : resourceFile.getChunks()) {
            System.out.println("ResourceTableChunk---------------------------------");
            if (chunk instanceof ResourceTableChunk) {
                System.out.println("----|StringPoolChunk--StringValue-------------------------------");
                StringPoolChunk stringPool = ((ResourceTableChunk) chunk).getStringPool();
                for (int i = 0; i < stringPool.getStringCount(); i++) {
                    if (i % 4 == 0) {
                        System.out.println(stringPool.getString(i) + "    ");
                    } else {
                        System.out.print(stringPool.getString(i) + "    ");
                    }
                }
                System.out.println();

                System.out.println("----|PackageChunk---------------------------------");
                for (PackageChunk packageChunk : ((ResourceTableChunk) chunk).getPackages()) {
                    //资源名称
                    System.out.println("---------|StringPoolChunk--keyName-------------------------------");
                    StringPoolChunk keyStringPool = packageChunk.getKeyStringPool();
                    for (int i = 0; i < keyStringPool.getStringCount(); i++) {
                        if (i % 4 == 0) {
                            System.out.println(keyStringPool.getString(i) + "    ");

                        } else {
                            System.out.print(stringPool.getString(i) + "    ");
                        }
                    }
                    System.out.println();
                    //资源类型名称
                    System.out.println("---------|StringPoolChunk--typeName-------------------------------");
                    StringPoolChunk typeStringPool = packageChunk.getTypeStringPool();
                    for (int i = 0; i < typeStringPool.getStringCount(); i++) {
                        System.out.print(typeStringPool.getString(i) + "    ");
                    }
                    System.out.println();

                    //ResTableTypeSpec
                    for (TypeSpecChunk typeSpecChunk : packageChunk.getTypeSpecChunks()) {
                        System.out.println("-------------|TypeSpecChunk---------------------------------");
                        Chunk.Type type = typeSpecChunk.getType();
                        String typeName = typeStringPool.getString(typeSpecChunk.getId() - 1);
                        System.out.println(type.name() + "--" + typeSpecChunk.getResourceCount() + "--" + typeName);
                        //ResTableType
                        System.out.println("------------------|TypeChunk---------------------------------");
                        for (TypeChunk typeChunk : packageChunk.getTypeChunks(typeName)) {
                            String config = typeChunk.getConfiguration().toString();
                            System.out.println(typeChunk.getTypeName() + "--" + config);
                            if (!typeName.equals("drawable"))continue;
                            for (Map.Entry<Integer, TypeChunk.Entry> entry : typeChunk.getEntries().entrySet()) {
                                System.out.println(entry.toString());
//                                try {
//                                    System.out.println("--" + entry.getValue().keyIndex() + "-" + stringPool.getString(entry.getValue().value().data()));
//                                }catch (Exception e){
//                                    System.out.println(e);
//                                }
                            }
                        }
                    }
                }
            }
        }
    }
}