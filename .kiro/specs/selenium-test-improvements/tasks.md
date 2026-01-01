# Implementation Plan: Selenium Test Improvements

## Overview

This implementation plan focuses on the highest-impact improvements to reach 95% grade. Tasks are prioritized by point value and implementation complexity, starting with sorting functionality (+5.56 points) followed by validation improvements (+3.33 points).

## Tasks

- [x] 1. Implement Task Sorting by Title (Item 12: 0→10 pts = +2.78)
  - [x] 1.1 Add sortByTitle method to TasksPage
    - Implement click action for title header
    - Add method to retrieve task titles in order
    - _Requirements: 1.1, 1.2_
  
  - [x] 1.2 Create title sorting validation methods
    - Add isTasksSortedByTitle() method to TasksPage
    - Implement alphabetical order validation logic
    - _Requirements: 1.2_
  
  - [x] 1.3 Implement "ordena las tareas por titulo" step
    - Add Java implementation in SortTaskSteps.java
    - Call TasksPage.sortByTitle() method
    - _Requirements: 1.1_
  
  - [x] 1.4 Implement "las tareas quedan ordenadas alfabeticamente por titulo" step
    - Add validation step in SortTaskSteps.java
    - Use TasksPage validation methods with assertions
    - _Requirements: 1.2_
  
  - [x] 1.5 Write property test for title sorting

    - **Property 1: Title Sorting Correctness**
    - **Validates: Requirements 1.1, 1.2**

- [x] 2. Implement Task Sorting by Date (Item 14: 0→10 pts = +2.78)
  - [x] 2.1 Add sortByEndDate method to TasksPage
    - Implement click action for end date header
    - Add method to retrieve task end dates in order
    - _Requirements: 2.1, 2.2_
  
  - [x] 2.2 Create date sorting validation methods
    - Add isTasksSortedByDate() method to TasksPage
    - Implement chronological order validation logic
    - _Requirements: 2.2_
  
  - [x] 2.3 Implement "ordena las tareas por fecha termino" step
    - Add Java implementation in SortTaskSteps.java
    - Call TasksPage.sortByEndDate() method
    - _Requirements: 2.1_
  
  - [x] 2.4 Implement "las tareas quedan ordenadas por fecha termino" step
    - Add validation step in SortTaskSteps.java
    - Use TasksPage validation methods with assertions
    - _Requirements: 2.2_
  
  - [x] 2.5 Write property test for date sorting

    - **Property 2: Date Sorting Correctness**
    - **Validates: Requirements 2.1, 2.2**

- [x] 3. Checkpoint - Verify sorting functionality
  - Ensure all sorting tests pass, ask the user if questions arise.

- [x] 4. Add Registration Validation (Item 7: 4→8 pts = +1.11)
  - [x] 4.1 Create registration success validation
    - Add validation method to check registration success indicators
    - Look for success message or page redirection
    - _Requirements: 3.1, 3.2_
  
  - [x] 4.2 Implement "el usuario se registra exitosamente" step
    - Add Then step in RegisterUserSteps.java
    - Use validation method with assertions
    - _Requirements: 3.1, 3.2_
  
  - [x] 4.3 Add registration error validation
    - Create method to validate error messages for invalid registration
    - Handle different error scenarios
    - _Requirements: 3.4_
  
  - [x]* 4.4 Write property test for registration validation
    - **Property 4: Registration Success Validation**
    - **Validates: Requirements 3.1, 3.2, 3.3**

- [x] 5. Add Login Validation (Item 8: 4→8 pts = +0.83)
  - [x] 5.1 Create login success validation
    - Add validation method to verify successful login
    - Check for home page redirection and user elements
    - _Requirements: 4.1, 4.2, 4.3_
  
  - [x] 5.2 Complete "se muestra la pantalla principal" step
    - Replace placeholder with actual validation logic
    - Use validation method with assertions
    - _Requirements: 4.1, 4.2_
  
  - [x] 5.3 Add login error validation
    - Create method to validate login error messages
    - Handle invalid credential scenarios
    - _Requirements: 4.4_
  
  - [x]* 5.4 Write property test for login validation
    - **Property 6: Login Success Validation**
    - **Validates: Requirements 4.1, 4.2, 4.3**

- [x] 6. Add Task Creation Validation (Item 11: 4→8 pts = +1.39)
  - [x] 6.1 Implement "el usuario crea una nueva tarea con datos validos" step
    - Add complete step implementation in CreateTaskSteps.java
    - Include opening modal, filling data, and submitting
    - _Requirements: 5.1_
  
  - [x] 6.2 Add proper validation for "la tarea se muestra en la lista" step
    - Replace current basic validation with comprehensive check
    - Verify task appears with correct title and data in task list
    - Verify task count increases by one
    - _Requirements: 5.1, 5.2, 5.3_
  
  - [x] 6.3 Enhance existing task creation validation
    - Improve "la tarea se muestra en la lista de tareas" step
    - Add assertions to verify task data persistence
    - _Requirements: 5.1, 5.2_
  
  - [x]* 6.4 Write property test for task creation
    - **Property 8: Task Creation Success**
    - **Validates: Requirements 5.1, 5.2, 5.3**

- [ ] 7. Checkpoint - Verify all validations
  - Ensure all validation tests pass, ask the user if questions arise.

- [ ] 8. Optimize Selectors for Performance (Critical Feedback)
  - [ ] 8.1 Fix RegisterUserModal selectors to use IDs
    - Replace XPath/CSS selectors with ID-based selectors
    - Use existing ID attributes instead of DOM traversal
    - Improve test execution speed and reliability
    - _Requirements: 6.1, 6.2_
  
  - [ ] 8.2 Review and optimize other page selectors
    - Update TasksPage, LoginPage, and other pages
    - Prioritize ID and CSS selectors over XPath
    - _Requirements: 6.1, 6.2_

- [ ] 9. Eliminate Hardcoded Data
  - [ ] 9.1 Create TestDataGenerator utility class
    - Generate unique emails, usernames, and task data
    - Ensure uniqueness across test runs
    - _Requirements: 7.1_
  
  - [ ] 9.2 Update steps to use generated data
    - Replace hardcoded values in RegisterUserSteps
    - Replace hardcoded values in CreateTaskSteps
    - _Requirements: 7.1_
  
  - [ ]* 9.3 Write property test for data uniqueness
    - **Property 10: Test Data Uniqueness**
    - **Validates: Requirements 7.1**

- [ ] 10. Implement Missing Steps
  - [ ] 10.1 Implement "el usuario accede a la lista de tareas" step
    - Add navigation logic to TasksSteps.java
    - Ensure proper page transition
    - _Requirements: 8.2_
  
  - [ ] 10.2 Complete any other missing step implementations
    - Review all feature files for unimplemented steps
    - Add corresponding Java implementations
    - _Requirements: 8.1, 8.4_

- [ ] 11. Final validation and cleanup
  - [ ] 11.1 Run complete test suite
    - Execute all tests to ensure no regressions
    - Verify all new functionality works correctly
  
  - [ ] 11.2 Fix README markdown formatting (Critical Feedback)
    - Update directory structure to use ```plaintext code blocks
    - Follow exact format: ```plaintext src └── test ... ```
    - _Requirements: README improvement_
  
  - [ ] 11.3 Review "Automatización modificación información usuario" 
    - Verify current validation is complete and robust
    - Add any missing assertions if needed
    - _Requirements: Item 9 optimization_

- [ ] 12. Final checkpoint - Complete project validation
  - Ensure all tests pass, verify grade improvement, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional property-based tests and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Priority order: Sorting (Items 12,14) → Validations (Items 7,8,11) → Quality improvements
- Expected point gain: +8.89 points minimum (69.45 total)
- Additional optimizations should push score above 70+ for comfortable pass