package observables

import java.beans.PropertyChangeSupport
import java.util.List
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.TransformationParticipant
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import java.beans.PropertyChangeListener

@Active(typeof(ObservableCompilationParticipant))
annotation Observable {
}

class ObservableCompilationParticipant implements TransformationParticipant<MutableClassDeclaration> {

	override doTransform(List<? extends MutableClassDeclaration> classes, extension TransformationContext context) {
		for (clazz : classes) {

			// For every field in a class annotated with @Observable, we generate a getter and a setter.
			// Additionally, the setter will fire change events. 
			for (f : clazz.declaredFields) {
				val fieldName = f.simpleName
				val fieldType = f.type

				clazz.addMethod('get' + fieldName.toFirstUpper) [
					returnType = fieldType
					body = ['''return this.�fieldName�;''']
				]

				clazz.addMethod('set' + fieldName.toFirstUpper) [
					addParameter(fieldName, fieldType)
					body = ['''
						�fieldType� _oldValue = this.�fieldName�;
						this.�fieldName� = �fieldName�;
						_propertyChangeSupport.firePropertyChange("�fieldName�", _oldValue, �fieldName�);
					''']
				]
			}

			// generated field to hold listeners, addPropertyChangeListener() and removePropertyChangeListener() 
			val changeSupportType = typeof(PropertyChangeSupport).newTypeReference
			clazz.addField("_propertyChangeSupport") [
				type = changeSupportType
				initializer = ['''new �toJavaCode(changeSupportType)�(this)''']
			]

			val propertyChangeListener = typeof(PropertyChangeListener).newTypeReference
			clazz.addMethod("addPropertyChangeListener") [
				addParameter("listener", propertyChangeListener)
				body = ['''this._propertyChangeSupport.addPropertyChangeListener(listener);''']
			]
			clazz.addMethod("removePropertyChangeListener") [
				addParameter("listener", propertyChangeListener)
				body = ['''this._propertyChangeSupport.removePropertyChangeListener(listener);''']
			]
		}
	}
}