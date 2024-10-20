// Display different sections based on navigation
function navigate(section) {
    const contentDiv = document.getElementById("content");
    switch (section) {
        case 'view-courses':
            fetchCourses();
            break;
        case 'add-course':
            displayCourseForm();
            break;
        case 'view-students':
            fetchStudents();
            break;
        case 'add-student':
            displayStudentForm();
            break;
        case 'view-teachers':
            fetchTeachers();
            break;
        case 'add-teacher':
            displayTeacherForm();
            break;
        case 'view-topics':
            fetchTopics();
            break;
        case 'add-topic':
            displayTopicForm();
            break;
        case 'search-courses-by-date':
            displaySearchCoursesByDateForm();
            break;
        case 'view-students-by-teacher':
            displayViewStudentsByTeacherForm();
            break;
    }
}

// Fetch and display courses
function fetchCourses() {
    fetch("http://localhost:8082/api/cursos")
        .then(response => response.json())
        .then(data => {
            displayCoursesTable(data);
        });
}

function displayCoursesTable(courses) {
    let content = `
        <h2>Cursos</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th><th>Tema</th><th>Docente</th><th>Fecha inicio</th><th>Fecha fin</th><th>Precio</th><th>Alumnos</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    courses.forEach(course => {
        content += `
            <tr>
                <td>${course.id}</td>
                <td>${course.tema.nombre}</td>
                <td>${course.docente.nombre}</td>
                <td>${course.fechaInicio}</td>
                <td>${course.fechaFin}</td>
                <td>${course.precio}</td>
                <td>${course.alumnos.map(alumno => alumno.nombre).join(', ')}</td>
                <td>
                    <button onclick="editCourse(${course.id})">Edit</button>
                    <button onclick="confirmDeleteCourse(${course.id})">Delete</button>
                </td>
            </tr>
        `;
    });

    content += `</tbody></table>`;
    document.getElementById("content").innerHTML = content;
}

// Confirm delete course
function confirmDeleteCourse(courseId) {
    if (confirm("Are you sure you want to delete this course?")) {
        deleteCourse(courseId);
    }
}

// Delete course
function deleteCourse(courseId) {
    fetch(`http://localhost:8082/api/cursos/${courseId}`, {
        method: "DELETE",
    })
        .then(response => {
            if (response.ok) {
                alert("Course deleted successfully.");
                fetchCourses();
            } else {
                alert("Error deleting course.");
            }
        });
}

// Display form to add or edit a course
function displayCourseForm(course = {}) {
    fetchTeachers().then(teachers => {
        fetchTopics().then(topics => {
            fetchStudents().then(students => {
                let formHtml = `
                    <h2>${course.id ? "Editar Curso" : "Agregar curso"}</h2>
                    <form onsubmit="saveCourse(event, ${course.id || ""})">
                        <label for="topic">Topic:</label>
                        <select id="topic" name="topic" required>
                            ${topics.map(topic => `<option value="${topic.id}" ${course.tema && course.tema.id === topic.id ? 'selected' : ''}>${topic.nombre}</option>`).join('')}
                        </select>

                        <label for="teacher">Teacher:</label>
                        <select id="teacher" name="teacher" required>
                            ${teachers.map(teacher => `<option value="${teacher.id}" ${course.docente && course.docente.id === teacher.id ? 'selected' : ''}>${teacher.nombre}</option>`).join('')}
                        </select>

                        <label for="startDate">Start Date:</label>
                        <input type="date" id="startDate" name="startDate" value="${course.fechaInicio || ""}" required>

                        <label for="endDate">End Date:</label>
                        <input type="date" id="endDate" name="endDate" value="${course.fechaFin || ""}" required>

                        <label for="price">Price:</label>
                        <input type="number" id="price" name="price" value="${course.precio || ""}" required>

                        <label for="students">Students:</label>
                        <select id="students" name="students" multiple>
                            ${students.map(student => `<option value="${student.id}" ${course.alumnos && course.alumnos.some(a => a.id === student.id) ? 'selected' : ''}>${student.nombre}</option>`).join('')}
                        </select>

                        <button type="submit">${course.id ? "Update" : "Save"}</button>
                    </form>
                `;
                document.getElementById("content").innerHTML = formHtml;
            });
        });
    });
}

// Save (POST/PUT) course
function saveCourse(event, courseId) {
    event.preventDefault();

    const courseData = {
        tema: {
            id: document.getElementById("topic").value,
        },
        docente: {
            id: document.getElementById("teacher").value,
        },
        fechaInicio: document.getElementById("startDate").value,
        fechaFin: document.getElementById("endDate").value,
        precio: parseFloat(document.getElementById("price").value),
        alumnos: Array.from(document.getElementById("students").selectedOptions).map(option => ({ id: option.value }))
    };

    const method = courseId ? "PUT" : "POST";
    const url = courseId ? `http://localhost:8082/api/cursos/${courseId}` : "http://localhost:8082/api/cursos";

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(courseData),
    })
        .then(response => response.json())
        .then(() => {
            alert("Course saved successfully.");
            fetchCourses();
        });
}

// Edit course
function editCourse(courseId) {
    fetch(`http://localhost:8082/api/cursos/${courseId}`)
        .then(response => response.json())
        .then(course => {
            displayCourseForm(course);
        });
}

// Fetch and display students
function fetchStudents() {
    return fetch("http://localhost:8082/api/alumnos")
        .then(response => response.json())
        .then(data => {
            displayStudentsTable(data);
            return data;
        });
}

function displayStudentsTable(students) {
    let content = `
        <h2>Alumnos</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th><th>Nombre</th><th>Fecha nacimiento</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    students.forEach(student => {
        content += `
            <tr>
                <td>${student.id}</td>
                <td>${student.nombre}</td>
                <td>${student.fechaNacimiento}</td>
                <td>
                    <button onclick="editStudent(${student.id})">Edit</button>
                    <button onclick="confirmDeleteStudent(${student.id})">Delete</button>
                </td>
            </tr>
        `;
    });

    content += `</tbody></table>`;
    document.getElementById("content").innerHTML = content;
}

// Display form to add or edit a student
function displayStudentForm(student = {}) {
    let formHtml = `
        <h2>${student.id ? "Editar alumnos" : "Agregar alumnos"}</h2>
        <form onsubmit="saveStudent(event, ${student.id || ""})">
            <label for="studentName">Name:</label>
            <input type="text" id="studentName" name="studentName" value="${student.nombre || ""}" required>

            <label for="studentBirthDate">Birth Date:</label>
            <input type="date" id="studentBirthDate" name="studentBirthDate" value="${student.fechaNacimiento || ""}" required>

            <button type="submit">${student.id ? "Update" : "Save"}</button>
        </form>
    `;
    document.getElementById("content").innerHTML = formHtml;
}

// Save (POST/PUT) student
function saveStudent(event, studentId) {
    event.preventDefault();

    const studentData = {
        nombre: document.getElementById("studentName").value,
        fechaNacimiento: document.getElementById("studentBirthDate").value,
    };

    const method = studentId ? "PUT" : "POST";
    const url = studentId ? `http://localhost:8082/api/alumnos/${studentId}` : "http://localhost:8082/api/alumnos";

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(studentData),
    })
        .then(response => response.json())
        .then(() => {
            alert("Student saved successfully.");
            fetchStudents();
        });
}

// Edit student
function editStudent(studentId) {
    fetch(`http://localhost:8082/api/alumnos/${studentId}`)
        .then(response => response.json())
        .then(student => {
            displayStudentForm(student);
        });
}

// Confirm delete student
function confirmDeleteStudent(studentId) {
    if (confirm("Are you sure you want to delete this student?")) {
        deleteStudent(studentId);
    }
}

// Delete student
function deleteStudent(studentId) {
    fetch(`http://localhost:8082/api/alumnos/${studentId}`, {
        method: "DELETE",
    })
        .then(response => {
            if (response.ok) {
                alert("Student deleted successfully.");
                fetchStudents();
            } else {
                alert("Error deleting student.");
            }
        });
}

// Fetch and display teachers
function fetchTeachers() {
    return fetch("http://localhost:8082/api/docentes")
        .then(response => response.json())
        .then(data => {
            displayTeachersTable(data);
            return data;
        });
}

function displayTeachersTable(teachers) {
    let content = `
        <h2>Docentes</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th><th>Nombre</th><th>Legajo</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    teachers.forEach(teacher => {
        content += `
            <tr>
                <td>${teacher.id}</td>
                <td>${teacher.nombre}</td>
                <td>${teacher.legajo}</td>
                <td>
                    <button onclick="editTeacher(${teacher.id})">Edit</button>
                    <button onclick="confirmDeleteTeacher(${teacher.id})">Delete</button>
                </td>
            </tr>
        `;
    });

    content += `</tbody></table>`;
    document.getElementById("content").innerHTML = content;
}

// Display form to add or edit a teacher
function displayTeacherForm(teacher = {}) {
    let formHtml = `
        <h2>${teacher.id ? "Editar docente" : "Agregar docente"}</h2>
        <form onsubmit="saveTeacher(event, ${teacher.id || ""})">
            <label for="teacherName">Name:</label>
            <input type="text" id="teacherName" name="teacherName" value="${teacher.nombre || ""}" required>

            <label for="teacherLegajo">File Number:</label>
            <input type="text" id="teacherLegajo" name="teacherLegajo" value="${teacher.legajo || ""}" required>

            <button type="submit">${teacher.id ? "Update" : "Save"}</button>
        </form>
    `;
    document.getElementById("content").innerHTML = formHtml;
}

// Save (POST/PUT) teacher
function saveTeacher(event, teacherId) {
    event.preventDefault();

    const teacherData = {
        nombre: document.getElementById("teacherName").value,
        legajo: document.getElementById("teacherLegajo").value,
    };

    const method = teacherId ? "PUT" : "POST";
    const url = teacherId ? `http://localhost:8082/api/docentes/${teacherId}` : "http://localhost:8082/api/docentes";

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(teacherData),
    })
        .then(response => response.json())
        .then(() => {
            alert("Teacher saved successfully.");
            fetchTeachers();
        });
}

// Edit teacher
function editTeacher(teacherId) {
    fetch(`http://localhost:8082/api/docentes/${teacherId}`)
        .then(response => response.json())
        .then(teacher => {
            displayTeacherForm(teacher);
        });
}

// Confirm delete teacher
function confirmDeleteTeacher(teacherId) {
    if (confirm("Are you sure you want to delete this teacher?")) {
        deleteTeacher(teacherId);
    }
}

// Delete teacher
function deleteTeacher(teacherId) {
    fetch(`http://localhost:8082/api/docentes/${teacherId}`, {
        method: "DELETE",
    })
        .then(response => {
            if (response.ok) {
                alert("Teacher deleted successfully.");
                fetchTeachers();
            } else {
                alert("Error deleting teacher.");
            }
        });
}

// Fetch and display topics
function fetchTopics() {
    return fetch("http://localhost:8082/api/temas")
        .then(response => response.json())
        .then(data => {
            displayTopicsTable(data);
            return data;
        });
}

function displayTopicsTable(topics) {
    let content = `
        <h2>Temas</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th><th>Nombre</th><th>Descripcion</th><th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;

    topics.forEach(topic => {
        content += `
            <tr>
                <td>${topic.id}</td>
                <td>${topic.nombre}</td>
                <td>${topic.descripcion}</td>
                <td>
                    <button onclick="editTopic(${topic.id})">Edit</button>
                    <button onclick="confirmDeleteTopic(${topic.id})">Delete</button>
                </td>
            </tr>
        `;
    });

    content += `</tbody></table>`;
    document.getElementById("content").innerHTML = content;
}

// Display form to add or edit a topic
function displayTopicForm(topic = {}) {
    let formHtml = `
        <h2>${topic.id ? "Editar tema" : "Agregar tema"}</h2>
        <form onsubmit="saveTopic(event, ${topic.id || ""})">
            <label for="topicName">Name:</label>
            <input type="text" id="topicName" name="topicName" value="${topic.nombre || ""}" required>

            <label for="topicDescription">Description:</label>
            <textarea id="topicDescription" name="topicDescription" required>${topic.descripcion || ""}</textarea>

            <button type="submit">${topic.id ? "Update" : "Save"}</button>
        </form>
    `;
    document.getElementById("content").innerHTML = formHtml;
}

// Save (POST/PUT) topic
function saveTopic(event, topicId) {
    event.preventDefault();

    const topicData = {
        nombre: document.getElementById("topicName").value,
        descripcion: document.getElementById("topicDescription").value,
    };

    const method = topicId ? "PUT" : "POST";
    const url = topicId ? `http://localhost:8082/api/temas/${topicId}` : "http://localhost:8082/api/temas";

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(topicData),
    })
        .then(response => response.json())
        .then(() => {
            alert("Topic saved successfully.");
            fetchTopics();
        });
}

// Edit topic
function editTopic(topicId) {
    fetch(`http://localhost:8082/api/temas/${topicId}`)
        .then(response => response.json())
        .then(topic => {
            displayTopicForm(topic);
        });
}

// Confirm delete topic
function confirmDeleteTopic(topicId) {
    if (confirm("Are you sure you want to delete this topic?")) {
        deleteTopic(topicId);
    }
}

// Delete topic
function deleteTopic(topicId) {
    fetch(`http://localhost:8082/api/temas/${topicId}`, {
        method: "DELETE",
    })
        .then(response => {
            if (response.ok) {
                alert("Topic deleted successfully.");
                fetchTopics();
            } else {
                alert("Error deleting topic.");
            }
        });
}

// Display form to search courses by end date
function displaySearchCoursesByDateForm() {
    let formHtml = `
        <h2>Buscar cursos por fecha de fin</h2>
        <form onsubmit="searchCoursesByDate(event)">
            <label for="endDate">Fecha de fin:</label>
            <input type="date" id="endDate" name="endDate" required>

            <button type="submit">Buscar</button>
        </form>
    `;
    document.getElementById("content").innerHTML = formHtml;
}

// Search courses by end date
function searchCoursesByDate(event) {
    event.preventDefault();
    const endDate = document.getElementById("endDate").value;
    fetch(`http://localhost:8082/api/cursos/fecha-fin?fecha=${endDate}`)
        .then(response => response.json())
        .then(data => {
            displayCoursesTable(data);
        });
}

// Display form to view students by teacher's legajo
function displayViewStudentsByTeacherForm() {
    let formHtml = `
        <h2>Ver alumnos por legajo del docente</h2>
        <form onsubmit="viewStudentsByTeacherLegajo(event)">
            <label for="teacherLegajo">Legajo del docente:</label>
            <input type="text" id="teacherLegajo" name="teacherLegajo" required>

            <button type="submit">Ver alumnos</button>
        </form>
    `;
    document.getElementById("content").innerHTML = formHtml;
}

// View students by teacher's legajo
function viewStudentsByTeacherLegajo(event) {
    event.preventDefault();
    const teacherLegajo = document.getElementById("teacherLegajo").value;
    fetch(`http://localhost:8082/api/cursos/docente/legajo/${teacherLegajo}/alumnos`)
        .then(response => response.json())
        .then(data => {
            displayStudentsTable(data);
        });
}

// Initial load
navigate('view-courses');