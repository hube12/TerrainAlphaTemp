public class Main {
    public static void main(String[] args) {
        GenerateChunk generateChunk=new GenerateChunk(10);
        byte[] chunk=generateChunk.provideChunk(0,0);
        for (int i = 0; i < 32768; i++) {
            System.out.print(chunk[i]+" ");
        }
        System.out.println();
        System.out.println();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int pos=128*16*x+128*z;
                int y;
                for (y = 127; y >= 0 && chunk[pos+y]==0; y--) ;
                System.out.print(y+" ");
            }
            System.out.println();
        }
    }


}
