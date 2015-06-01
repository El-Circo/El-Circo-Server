Chat
	head (message id)
	from (student name)
	to (student name or curso)
	metadata ([gcm from, gcm to(or curso), head])
	message
Materias
	{
		Materia: nameMateria,
		Tipo: prueba|tarea,
		Fecha: dd/mm/yyyy
		Mensaje: 1024 char max
	}
Horarios
	Horarios en una tabla. Simple enough
Perdido
	{
		Sonar alarma
		Obtener nombre del wifi y comprobar si es del colegio
		Si es del colegio, avisar el curso
		Si no es del colegio, avisar que no esta en el cole
		Obtener localizacion y mostrar mapa
	}

Informacion
	gcm registration id
	student name
	curso
	head (last message received)
	wifi name
	last location	
	alarm on
Comandos
	AlarmOn
	GPSOn
	SyncAll 
	SQLCompact
	SQLInsert
	SQLRemove
	ChangeTestRegID
	RunTest
	
