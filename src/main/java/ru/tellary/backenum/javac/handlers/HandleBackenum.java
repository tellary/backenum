package ru.tellary.backenum.javac.handlers;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import org.kohsuke.MetaInfServices;
import ru.tellary.backenum.Backenum;

@MetaInfServices(JavacAnnotationHandler.class)
public class HandleBackenum extends JavacAnnotationHandler<Backenum>{
    @Override
    public void handle(
        AnnotationValues<Backenum> annotation,
        JCAnnotation ast,
        JavacNode annotationNode) {
    }
}
