<h1>🎨 CREATIVEWORK MANAGER</h1>

<h2>기획의도 (Project Background)</h2>

<p>
창작 활동을 관리하는 과정에서, 커미션 일정은 크레페(Crepe), 아트머그(ArtMug), 지출 내역은 일반 가계부 앱 등 
<b>서로 다른 플랫폼에 흩어져 관리되는 단절된 경험</b>에서 출발했습니다.<br /><br />
<b>CreativeWork Manager</b>는 이러한 단절을 해결하기 위해, 
<b>소비(Expense)–프로젝트(Project)–일정(Schedule)–리포트(Report)</b>의 
흐름을 하나의 콘솔 내에서 유기적으로 연결하도록 설계된 자바 기반 통합 관리 시스템입니다.
</p>

<p>
Crepe의 거래 중심 일정 관리, ArtMug의 포트폴리오 중심 기록, 
가계부 앱의 금전 관리 기능을 비교 분석하여 
<b>‘기록–관리–분석’의 통합적 흐름</b>을 구현했습니다.<br />
창작자는 자신의 소비, 프로젝트, 일정, 성과를 한눈에 파악하고 스스로 관리할 수 있습니다.
</p>

<p>
객체지향 구조(OOP)를 중심으로 설계하였으며, 
<code>Work</code> 추상 클래스와 <code>Schedulable</code> 인터페이스를 통해 
공통 속성과 행위를 구조화했습니다.<br />
또한 <code>ArrayList</code>와 <code>HashMap</code>으로 
가변적 데이터 구조를 구성하고, <b>파일 입출력 및 예외 처리</b>를 통해 
데이터의 지속성과 안정성을 확보했습니다.
</p>

<hr />

<h2> 프로젝트 기간</h2>
<p><b>2025.10.31 ~ 2025.11.04</b></p>

<hr />

<h2> 주요 기능 (Key Features)</h2>

<h3>1. 소비 내역 관리 (Expense Management)</h3>
<ul>
  <li>소비 항목 등록 (이름, 분류, 금액, 구매일, 상태)</li>
  <li><b>분류별 / 월별 필터링</b>, 총합 및 평균 금액 자동 계산</li>
  <li><b>정렬 기능</b> (날짜순, 금액순)으로 소비 패턴 분석</li>
  <li>파일 저장 (<code>expense.txt</code>) 및 자동 불러오기 기능</li>
</ul>

<h3>2. 프로젝트 일정 관리 (Project Management)</h3>
<ul>
  <li>프로젝트 등록 (제목, 담당자, 시작일, 마감일, 상태)</li>
  <li><b>D-Day 자동 계산</b> 및 <b>마감 임박 알림</b> 기능</li>
  <li>상태 변경 (진행중 ↔ 완료 ↔ 보류), 완료율(%) 계산</li>
  <li><code>LocalDate</code>, <code>Period API</code> 기반의 시간 관리 로직</li>
</ul>

<h3>3. 일정 캘린더 (Schedule Tracker)</h3>
<ul>
  <li>일정 등록 (행사명, 날짜, 메모) 및 월별 조회</li>
  <li><b>Schedulable 인터페이스</b>로 남은 일수 계산 기능 강제</li>
  <li><code>Map&lt;String, Schedule&gt;</code> 구조로 빠른 검색 및 중복 방지</li>
  <li>월별 D-Day 리스트 출력</li>
</ul>

<h3>4. 월간 활동 리포트 (Activity Report)</h3>
<ul>
  <li><b>소비 총액, 완료 프로젝트, 등록 일정 수</b> 자동 집계</li>
  <li><b>Stream API</b>를 활용한 필터링·집계 로직</li>
  <li>콘솔 및 파일 (<code>report_YYYY_MM.txt</code>) 출력 지원</li>
</ul>

<h3>5. 파일 입출력 & 예외 처리 (Persistence & Exception Handling)</h3>
<ul>
  <li><code>BufferedReader</code>, <code>BufferedWriter</code> 기반 저장/불러오기</li>
  <li><code>try-catch</code>로 <b>입력 오류</b>(NumberFormatException, DateTimeParseException) 및 
  <b>파일 접근 오류</b>(IOException) 안전 처리</li>
</ul>

<hr />

<h2> 기술 스택 (Tech Stack)</h2>
<ul>
  <li><b>Language:</b> Java (JDK 17)</li>
  <li><b>Paradigm:</b> Object-Oriented Programming (Inheritance + Interface)</li>
  <li><b>Data Structure:</b> ArrayList, HashMap</li>
  <li><b>Persistence:</b> File I/O (BufferedReader, BufferedWriter)</li>
  <li><b>Error Handling:</b> try-catch Exception Handling</li>
</ul>

<hr />

<h2> 설계 개요 (Design Overview)</h2>
<ul>
  <li><b>Work (추상 클래스)</b> → Expense / Project / Schedule의 공통 필드(title, status)</li>
  <li><b>Schedulable (인터페이스)</b> → 모든 일정 객체가 남은 일수를 계산하도록 강제</li>
  <li><b>Composition 구조</b> → ProjectService, ExpenseService 등에서 <code>DataStore</code>를 주입받아 관리</li>
</ul>

<hr />

<h2>📎 문서</h2>
<p>
(📁 <a href="https://www.notion.so/evve00/2a19bf07f8d0809faeffeb8d8284a6ac?source=copy_link" target="_blank">프로젝트 문서 바로가기</a>)
</p>

<hr />

<h2> 상속(extends)과 인터페이스(implements) 설계 의도</h2>

<h3> 1. 상속 — 공통 속성의 구조화 (Inheritance)</h3>

<p>
<code>Work</code> 추상 클래스는 “창작 활동(Work)”이라는 상위 개념을 <b>추상화한 부모 클래스</b>입니다.<br />
모든 세부 항목인 <code>Expense</code>, <code>Project</code> 등은 ‘Work’의 일종이라는 공통점을 가지므로, 
<code>title</code>과 <code>status</code> 등의 공통 필드를 상속받도록 설계했습니다.
</p>

<ul>
  <li>코드 중복을 줄이고 유지보수를 용이하게 함</li>
  <li>새로운 관리 항목 (예: <code>Exhibition</code>, <code>Commission</code> 등)도 <b>확장성 있게 추가 가능</b></li>
  <li>다형성(Polymorphism)을 활용해 <code>List&lt;Work&gt;</code> 형태로 다양한 객체를 일괄 관리 가능</li>
</ul>

<p><b>효과:</b><br />
데이터 구조가 일관되고, 확장성과 재사용성이 높은 객체 지향적 구조를 실현했습니다.
</p>

<hr style="border: 0; border-top: 1px solid #ddd;" />

<h3> 2. 인터페이스 — 일정성(스케줄 기능)의 통합 규약 (Interface)</h3>

<p>
일정 관련 클래스(<code>Schedule</code>)에는 <b>D-Day 계산</b>이나 <b>남은 일수 반환</b>과 같은 공통 행위가 필요했습니다.<br />
이를 표준화하기 위해 <code>Schedulable</code> 인터페이스를 정의하여, 
모든 일정형 객체가 <code>getRemainingDays()</code> 메서드를 반드시 구현하도록 강제했습니다.
</p>

<ul>
  <li><code>Schedule</code>, <code>Project</code> 등 다양한 클래스가 동일한 스케줄 규약을 따름</li>
  <li>“남은 기간 출력” 로직을 한 번만 작성해도 모든 일정형 객체에 공통 적용 가능</li>
  <li>공통 스케줄 기능을 <b>효율적으로 재사용</b>할 수 있음</li>
</ul>

<p><b>효과:</b><br />
일정 데이터의 <b>일관성·확장성·유지보수성</b>을 확보하며, 
객체 간의 역할을 명확히 구분하는 설계가 가능했습니다.
</p>

<hr />

## 클래스 다이어그램
<img width="1844" height="1566" alt="Image" src="https://github.com/user-attachments/assets/857649b0-ae42-4a02-8bec-4a766714efe8" />

<hr />

<h2>🧯 트러블슈팅 (Troubleshooting)</h2>

<h3>1. 일정 삭제 기능 구현 중 HashMap으로는 삭제/순서 관리가 어려웠던 문제</h3>

<h4> 증상</h4>
<ul>
  <li>일정(Schedule) 등록 후, <b>등록 순서대로</b> 목록을 보여주고 싶었음</li>
  <li>사용자가 “번호로 선택해 삭제”하는 기능을 구현하려 했지만, <code>HashMap</code> 사용 시 순서가 유지되지 않음</li>
  <li>콘솔 출력 시마다 일정의 순서가 뒤섞여 표시됨</li>
</ul>

<h4> 원인 분석</h4>

<ol>
  <li><b>HashMap의 특성</b><br />
    - <code>HashMap</code>은 내부적으로 해시 기반 구조를 사용하므로 <b>입력 순서를 보장하지 않음</b>.<br />
    - 따라서 <code>for-each</code>나 <code>entrySet()</code>으로 순회 시, 저장 순서와 무관하게 임의의 순서로 출력됨.
  </li>

  <li><b>사용 목적과의 불일치</b><br />
    - <code>List</code>처럼 순서를 유지하되, <code>Map</code>의 빠른 검색 기능도 필요했음.
  </li>
</ol>

<h4> 해결 과정</h4>

<ol>
  <li><b>자료구조 교체: HashMap → LinkedHashMap</b><br />
    - <code>LinkedHashMap</code>은 내부적으로 <b>이중 연결 리스트</b>를 사용해 삽입 순서를 유지함.<br />
    - 이를 통해 “등록 순서대로 출력 + Key 기반 검색”을 동시에 구현할 수 있음.
  </li>

  <li><b>코드 수정</b></li>
</ol>


```java
// 기존
private final Map<String, Schedule> schedules = new HashMap<>();

// 변경 후
private final Map<String, Schedule> schedules = new LinkedHashMap<>();

```

<h3>2. 재시작 후 <code>Schedule</code>만 복구되고 <code>Project</code>/<code>Expense</code>는 비어 보이는 문제</h3>

<h4> 증상</h4>
<ul>
  <li>앱 종료 후 다시 실행하면 <b>Schedule</b>만 정상 복원</li>
  <li><b>Project</b>, <b>Expense</b> 목록은 항상 빈 목록처럼 표시</li>
</ul>

<h4> 원인 분석</h4>
<ol>
  <li><b>파일명 불일치</b><br />
    저장 시: <code>data/expenses.txt</code> (복수)<br />
    로드 시: <code>data/expense.txt</code> (단수)<br />
    → 저장은 되지만, 재시작 후 <b>다른 파일</b>을 읽어 항상 빈 목록처럼 보임.
  </li>
  <li><b>구분자 불일치</b><br />
    저장 시: <code>,</code> (CSV)<br />
    로드 시: <code>|</code> (파이프)<br />
    → 같은 파일이라도 포맷이 달라 <b>파싱 실패</b>.
  </li>
</ol>

<h4> 해결 전략</h4>
<ol>
  <li><b>파일명/경로/구분자 상수화</b> — <code>FileManager</code> 단일 소스에서만 관리</li>
  <li><b>직렬화/역직렬화 규약 고정</b> — <code>toLine()</code> / <code>fromLine()</code>를 각 도메인에서 일관 구현</li>
  <li><b>로딩 실패 로깅</b> — 스킵한 라인/예외를 로깅해 원인 추적 가능하게 함</li>
</ol>

<h4> 코드 스니펫 (핵심 수정)</h4>

<p><b>1) FileManager 상수 통일</b></p>

```java
public final class FileManager {
    private static final String DIR = "data";
    private static final String DELIM = ","; // 저장/로딩 동일 구분자

    private static final String EXPENSES_FILE = DIR + "/expenses.txt";
    private static final String PROJECTS_FILE = DIR + "/projects.txt";
    private static final String SCHEDULES_FILE = DIR + "/schedules.txt";
    // ... 생성자에서 DIR 존재 확인 및 생성
}



