package com.attend.data

import com.attend.common.base.BaseRepository
import com.attend.data.models.sections.*
import com.attend.data.models.users.Admin
import com.attend.data.models.users.Student
import com.attend.data.models.users.Teacher
import com.google.firebase.firestore.Query

class AppRepository : BaseRepository() {

    companion object {
        // For Singleton instantiation
        @Volatile
        private var INSTANCE: AppRepository? = null
        fun getInstance(): AppRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppRepository().also { INSTANCE = it }
            }
        }
    }

    // Admin  ======================================================================================


    // Users  ======================================================================================
    // Users\Admin\Auth=============================================================================
    fun authAdminLogin(phone: String, password: String, response: (Admin?) -> Unit) {
        dbShow(
            db.collection("admins").whereEqualTo("phone", phone)
                .whereEqualTo("password", password), Admin::class.java, response
        )
    }

    fun getAdminByPhone(phone: String, response: (Admin?) -> Unit) {
        dbShow(db.collection("admins").whereEqualTo("phone", phone), Admin::class.java, response)
    }

    fun getAdmins(response: (ArrayList<Admin>) -> Unit) {
        val query: Query = db.collection("admins")
        dbRead(query, Admin::class.java, response)
    }

    fun getAdmin(id: String, response: (Admin?) -> Unit) {
        dbShow(db.collection("admins").document(id), Admin::class.java, response)
    }

    fun getAdminByName(name: String, response: (Admin?) -> Unit) {
        dbShow(db.collection("admins").whereEqualTo("name", name), Admin::class.java, response)
    }

    fun insertAdmin(data: Admin, response: (String) -> Unit) {
        dbInsert(db.collection("admins"), data, response)
    }

    fun updateAdmin(data: Admin, response: (Boolean) -> Unit) {
        dbUpdate(db.collection("admins").document(data.id), data, response)
    }

    fun deleteAdmin(id: String, response: (Boolean) -> Unit) {
        dbDelete(db.collection("admins").document(id), response)
    }


    // Users\Teacher\Auth===========================================================================
    fun authTeacherLogin(phone: String, password: String, response: (Teacher?) -> Unit) {
        dbShow(
            db.collection("teachers").whereEqualTo("phone", phone)
                .whereEqualTo("password", password), Teacher::class.java, response
        )
    }

    fun getTeachers(response: (ArrayList<Teacher>) -> Unit) {
        val query: Query = db.collection("teachers")
        dbRead(query, Teacher::class.java, response)
    }

    fun getTeacher(id: String, response: (Teacher?) -> Unit) {
        dbShow(db.collection("teachers").document(id), Teacher::class.java, response)
    }

    fun getTeacherByPhone(phone: String, response: (Teacher?) -> Unit) {
        dbShow(
            db.collection("teachers").whereEqualTo("phone", phone),
            Teacher::class.java,
            response
        )
    }

    fun getTeacherByName(name: String, response: (Teacher?) -> Unit) {
        dbShow(db.collection("teachers").whereEqualTo("name", name), Teacher::class.java, response)
    }

    fun insertTeacher(data: Teacher, response: (String) -> Unit) {
        dbInsert(db.collection("teachers"), data, response)
    }

    fun updateTeacher(data: Teacher, response: (Boolean) -> Unit) {
        dbUpdate(db.collection("teachers").document(data.id), data, response)
    }

    fun deleteTeacher(id: String, response: (Boolean) -> Unit) {
        dbDelete(db.collection("teachers").document(id), response)
    }

    // Users\Student\Auth===========================================================================

    fun authStudentLogin(phone: String, password: String, response: (Student?) -> Unit) {
        dbShow(
            db.collection("students").whereEqualTo("phone", phone)
                .whereEqualTo("password", password), Student::class.java, response
        )
    }

    // Student
    fun getStudents(response: (ArrayList<Student>) -> Unit) {
        dbRead(db.collection("students"), Student::class.java, response)
    }

    fun getStudent(id: String, response: (Student?) -> Unit) {
        dbShow(db.collection("students").document(id), Student::class.java, response)
    }

    fun getStudentByPhone(phone: String, response: (Student?) -> Unit) {
        dbShow(
            db.collection("students").whereEqualTo("phone", phone),
            Student::class.java,
            response
        )
    }

    fun insertStudent(student: Student, response: (String) -> Unit) {
        dbInsert(db.collection("students"), student, response)
    }

    fun updateStudent(Student: Student, response: (Boolean) -> Unit) {
        dbUpdate(db.collection("students").document(Student.id), Student, response)
    }

    fun deleteStudent(id: String, response: (Boolean) -> Unit) {
        dbDelete(db.collection("students").document(id), response)
    }


    // Sections  ===================================================================================
    // Sections\Section=============================================================================
    fun getSections(response: (ArrayList<Section>) -> Unit) {
        val query: Query = db.collection("sections")
        dbRead(query, Section::class.java, response)
    }

    fun getSection(id: String, response: (Section?) -> Unit) {
        dbShow(db.collection("sections").document(id), Section::class.java, response)
    }

    fun getSectionByCode(code: String, response: (Section?) -> Unit) {
        dbShow(db.collection("sections").whereEqualTo("code", code), Section::class.java, response)
    }

    fun insertSection(data: Section, response: (String) -> Unit) {
        dbInsert(db.collection("sections"), data, response)
    }

    fun updateSection(data: Section, response: (Boolean) -> Unit) {
        dbUpdate(db.collection("sections").document(data.id), data, response)
    }

    fun deleteSection(id: String, response: (Boolean) -> Unit) {
        dbDelete(db.collection("sections").document(id), response)
    }

    // Sections\Subject=============================================================================
    fun getSubjects(
        sectionID: String? = null,
        teacherID: String? = null,
        response: (ArrayList<Subject>) -> Unit
    ) {
        var query: Query = db.collection("subjects")
        if (sectionID != null)
            query = db.collection("subjects").whereEqualTo("sectionID", sectionID)
        if (teacherID != null)
            query = db.collection("subjects").whereEqualTo("teacherID", teacherID)
        dbRead(query, Subject::class.java, response)
    }

    fun getSubject(id: String, response: (Subject?) -> Unit) {
        dbShow(db.collection("subjects").document(id), Subject::class.java, response)
    }

    fun getSubjectByCode(code: String, response: (Subject?) -> Unit) {
        dbShow(db.collection("subjects").whereEqualTo("code", code), Subject::class.java, response)
    }

    fun insertSubject(data: Subject, response: (String) -> Unit) {
        dbInsert(db.collection("subjects"), data, response)
    }

    fun updateSubject(data: Subject, response: (Boolean) -> Unit) {
        dbUpdate(db.collection("subjects").document(data.id), data, response)
    }

    fun deleteSubject(id: String, response: (Boolean) -> Unit) {
        dbDelete(db.collection("subjects").document(id), response)
    }


    // Sections\Lecture=============================================================================
    fun getLectures(
        sectionID: String? = null,
        teacherID: String? = null,
        subjectIDs: List<String>? = null,
        subjectID: String? = null,
        date: String? = null,
        response: (ArrayList<Lecture>) -> Unit
    ) {
        var query: Query = db.collection("lectures")
        if (sectionID != null) query = query.whereEqualTo("sectionID", sectionID)
        if (teacherID != null) query = query.whereEqualTo("teacherID", teacherID)
        if (subjectID != null) query = query.whereEqualTo("subjectID", subjectID)
        if (subjectIDs != null) query = query.whereIn("subjectID", subjectIDs)
            .whereEqualTo("date", date)

        dbRead(query, Lecture::class.java, response)
    }

    fun getLecture(id: String, response: (Lecture?) -> Unit) {
        dbShow(db.collection("lectures").document(id), Lecture::class.java, response)
    }

    fun insertLecture(data: Lecture, response: (String) -> Unit) {
        dbInsert(db.collection("lectures"), data, response)
    }

    fun updateLecture(data: Lecture, response: (Boolean) -> Unit) {
        dbUpdate(db.collection("lectures").document(data.id), data, response)
    }

    fun deleteLecture(id: String, response: (Boolean) -> Unit) {
        dbDelete(db.collection("lectures").document(id), response)
    }


    // Sections\SubjectStudents=============================================================================
    fun getSubjectStudents(
        subjectID: String? = null,
        studentID: String? = null,
        response: (ArrayList<SubjectStudent>) -> Unit
    ) {
        var query: Query = db.collection("subjectStudents")
        if (subjectID != null) query = query.whereEqualTo("subjectID", subjectID)
        if (studentID != null) query = query.whereEqualTo("studentID", studentID)
        dbRead(query, SubjectStudent::class.java, response)
    }

    fun checkSubjectStudentExist(
        studentID: String?,
        subjectID: String?,
        response: (SubjectStudent?) -> Unit
    ) {
        dbShow(
            db.collection("subjectStudents").whereEqualTo("studentID", studentID)
                .whereEqualTo("subjectID", subjectID), SubjectStudent::class.java, response
        )
    }

    fun getSubjectStudent(id: String, response: (SubjectStudent?) -> Unit) {
        dbShow(db.collection("subjectStudents").document(id), SubjectStudent::class.java, response)
    }

    fun insertSubjectStudent(data: SubjectStudent, response: (String) -> Unit) {
        dbInsert(db.collection("subjectStudents"), data, response)
    }

    fun updateSubjectStudent(data: SubjectStudent, response: (Boolean) -> Unit) {
        dbUpdate(db.collection("subjectStudents").document(data.id), data, response)
    }

    fun deleteSubjectStudent(id: String, response: (Boolean) -> Unit) {
        dbDelete(db.collection("subjectStudents").document(id), response)
    }


    // Sections\LectureAttendance=============================================================================
    fun getLectureAttendances(
        lectureID: String? = null,
        studentID: String? = null,
        subjectID: String? = null,
        response: (ArrayList<LectureAttendance>) -> Unit
    ) {
        var query: Query = db.collection("lectureattendance")
        if (lectureID != null) query = query.whereEqualTo("lectureID", lectureID)
        if (studentID != null && subjectID != null)
            query = query.whereEqualTo("studentID", studentID).whereEqualTo("subjectID", subjectID)
        dbRead(query, LectureAttendance::class.java, response)
    }

    fun checkLectureAttendanceExist(
        studentID: String?,
        lectureID: String?,
        response: (LectureAttendance?) -> Unit
    ) {
        dbShow(
            db.collection("lectureattendance").whereEqualTo("studentID", studentID)
                .whereEqualTo("lectureID", lectureID), LectureAttendance::class.java, response
        )
    }

    fun getLectureAttendance(id: String, response: (LectureAttendance?) -> Unit) {
        dbShow(
            db.collection("lectureattendance").document(id),
            LectureAttendance::class.java,
            response
        )
    }

    fun insertLectureAttendance(data: LectureAttendance, response: (String) -> Unit) {
        dbInsert(db.collection("lectureattendance"), data, response)
    }

    fun updateLectureAttendance(data: LectureAttendance, response: (Boolean) -> Unit) {
        dbUpdate(db.collection("lectureattendance").document(data.id), data, response)
    }

    fun deleteLectureAttendance(id: String, response: (Boolean) -> Unit) {
        dbDelete(db.collection("lectureattendance").document(id), response)
    }
}