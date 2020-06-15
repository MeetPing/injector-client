package debug.client.injection;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

@HookClass("dbn")
public class MinecraftTransformer extends injector {


    @Override
    public void inject(ClassReader classReader, ClassNode classNode) {
for (MethodNode methodNode : (List<MethodNode>) classNode.methods) {
    if(methodNode.name.equals("s") && methodNode.desc.equals("()V") ) {
        // Idk what that is ^^^im just following the tutorial and its not the same mappings. its 1.8.9 mine is 1.15.2
    InsnList insnList =  new InsnList();
    }
}
    }
}
