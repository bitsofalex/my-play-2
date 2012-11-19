function TodoCtrl($scope, Test) {
	var google = {text: 'do spike', done: true};
	
	$scope.totalTodos = 4;
	
	$scope.todos = Test.query(google);
	
	$scope.autocomplete = function() {
		alert("test");
	}
}