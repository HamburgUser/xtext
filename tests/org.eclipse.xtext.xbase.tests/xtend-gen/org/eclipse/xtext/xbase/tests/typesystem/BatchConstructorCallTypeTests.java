package org.eclipse.xtext.xbase.tests.typesystem;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.XConstructorCall;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.eclipse.xtext.xbase.tests.typesystem.AbstractConstructorCallTypeTest;
import org.eclipse.xtext.xbase.typesystem.IBatchTypeResolver;
import org.eclipse.xtext.xbase.typesystem.IResolvedTypes;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;
import org.junit.Assert;

/**
 * @author Sebastian Zarnekow
 */
@SuppressWarnings("all")
public class BatchConstructorCallTypeTests extends AbstractConstructorCallTypeTest {
  @Inject
  private IBatchTypeResolver typeResolver;
  
  public void resolvesConstructorCallsTo(final String expression, final String... types) {
    final String expressionWithQualifiedNames = expression.replace("$$", "org::eclipse::xtext::xbase::lib::");
    final List<XConstructorCall> featureCalls = this.findConstructorCalls(expressionWithQualifiedNames);
    boolean _isEmpty = featureCalls.isEmpty();
    Assert.assertFalse(_isEmpty);
    int _size = ((List<String>)Conversions.doWrapArray(types)).size();
    int _size_1 = featureCalls.size();
    Assert.assertEquals(_size, _size_1);
    XConstructorCall _head = IterableExtensions.<XConstructorCall>head(featureCalls);
    final IResolvedTypes resolvedTypes = this.typeResolver.resolveTypes(_head);
    final Procedure2<XConstructorCall,Integer> _function = new Procedure2<XConstructorCall,Integer>() {
        public void apply(final XConstructorCall featureCall, final Integer index) {
          final LightweightTypeReference type = resolvedTypes.getActualType(featureCall);
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("failed for closure at ");
          _builder.append(index, "");
          String _get = ((List<String>)Conversions.doWrapArray(types)).get((index).intValue());
          String _simpleName = type.getSimpleName();
          Assert.assertEquals(_builder.toString(), _get, _simpleName);
        }
      };
    IterableExtensions.<XConstructorCall>forEach(featureCalls, _function);
  }
}