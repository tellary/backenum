package ru.tellary.backenum.javac.handlers;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import lombok.core.AnnotationValues;
import lombok.core.AST.Kind;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import org.kohsuke.MetaInfServices;
import ru.tellary.backenum.Backenum;

import static com.sun.tools.javac.util.List.nil;

@MetaInfServices(JavacAnnotationHandler.class)
public class HandleBackenum extends JavacAnnotationHandler<Backenum>{
    @Override
    public void handle(
            AnnotationValues<Backenum> annotation,
            JCAnnotation ast,
            JavacNode annotationNode) {
        JavacNode enumClass = annotationNode.up();
        if (!isEnumType(enumClass))
            annotationNode.addError("@Backenum is only supported on an enum");

        JavacTreeMaker enumTM = enumClass.getTreeMaker();
        addInnerClass(enumClass, enumTM);
    }

    private JavacNode addInnerClass(
            JavacNode enumClass,
            JavacTreeMaker enumTM) {
        JCModifiers modifiers = enumTM
            .Modifiers(Flags.PUBLIC | Flags.STATIC);
        String innerClassName = "Backward";
        JCClassDecl innerClassDecl = enumTM
            .ClassDef(modifiers, enumClass.toName(innerClassName),
                      nil(), null, nil(), nil());
        return JavacHandlerUtil.injectType(enumClass, innerClassDecl);
    }

    // Copied from JavacNode.
    // The methods are only available in later lombok
    // versions with "Shadow ClassLoader"
	private static boolean isEnumType(JavacNode self) {
        JCModifiers mods;
        JCTree node = self.get();
        if (node instanceof JCClassDecl)
            mods = ((JCClassDecl) node).getModifiers();
        else return false;

		if (self.getKind() != Kind.TYPE) return false;
		return mods != null && (Flags.ENUM & mods.flags) != 0;
	}
}
