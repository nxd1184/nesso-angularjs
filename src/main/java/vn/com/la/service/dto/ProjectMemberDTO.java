package vn.com.la.service.dto;


import java.util.*;

public class ProjectMemberDTO {

    private Map<Long, ProjectMember> projects = new HashMap<>();
    private List<Member> members = new ArrayList<>();

    public static ProjectMemberDTO processDataset(List<ProductionBonusDTO> productionBonusDTOS) {
        ProjectMemberDTO pDTO = new ProjectMemberDTO();
        Set<Member> members = new HashSet<>();

        for (ProductionBonusDTO pb : productionBonusDTOS) {
            Member m = new Member(
                pb.getUserId(),
                pb.getEmployee(),
                pb.getVolumn(),
                pb.getTotalCredit()
            );

            members.add(m);
            if (pDTO.projects.containsKey(pb.getProjectId())) {
                Map<Long, JobMember> jobs = pDTO.projects.get(pb.getProjectId()).jobs;
                if (jobs.containsKey(pb.getJobId())) {
                    jobs.get(pb.getJobId()).users.put(m.getId(), m);
                } else {
                    JobMember jm = new JobMember(pb.getJobId(), pb.getCredit(), pb.getJobName());
                    jm.users.put(m.getId(), m);
                    jobs.put(jm.getId(), jm);
                }
            } else {
                ProjectMember pm = new ProjectMember(pb.getProjectId(), pb.getProjectName());
                JobMember jm = new JobMember(pb.getJobId(), pb.getCredit(), pb.getJobName());
                jm.users.put(m.getId(), m);
                pm.jobs.put(jm.getId(), jm);
                pDTO.projects.put(pm.getId(), pm);
            }
        }
        pDTO.members.addAll(members);
        return pDTO;
    }

    public Map<Long, ProjectMember> getProjects() {
        return projects;
    }

    public void setProjects(Map<Long, ProjectMember> projects) {
        this.projects = projects;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public static class ProjectMember {

        private Long id;
        private String name;
        private Map<Long, JobMember> jobs = new HashMap<>();

        public ProjectMember() {

        }

        public ProjectMember(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<Long, JobMember> getJobs() {
            return jobs;
        }

        public void setJobs(Map<Long, JobMember> jobs) {
            this.jobs = jobs;
        }

    }


    public static class JobMember {
        private Long id;
        private Long credit;
        private String name;
        private Map<Long, Member> users = new HashMap<>();

        public JobMember() {

        }

        public JobMember(Long id, Long credit, String name) {
            this.id = id;
            this.credit = credit;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getCredit() {
            return credit;
        }

        public void setCredit(Long credit) {
            this.credit = credit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<Long, Member> getUsers() {
            return users;
        }

        public void setUsers(Map<Long, Member> users) {
            this.users = users;
        }
    }

    public static class Member {
        Long id;
        String userName;
        Long done;
        Long volume;

        public Member() {

        }

        public Member(Long id, String userName, Long done, Long volume) {
            this.id = id;
            this.userName = userName;
            this.done = done;
            this.volume = volume;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!Member.class.isAssignableFrom(obj.getClass())) {
                return false;
            }
            return this.id.equals(((Member) obj).id);
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Long getDone() {
            return done;
        }

        public void setDone(Long done) {
            this.done = done;
        }

        public Long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }
    }
}
